package initializer;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.system.JmeCanvasContext;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;
import jme3tools.navmesh.NavMesh;
import jme3tools.navmesh.util.NavMeshGenerator;
import population.Population;
import population.PersonCategory;
import java.awt.Insets;
import java.util.Map;
import java.util.HashMap;

import java.util.ArrayList;

import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.AbstractTableModel;
import java.util.EventObject;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import java.awt.Container;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTabbedPane;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.event.ChangeEvent;
/**
 *
 */
public class Main extends SimpleApplication {
    
    //Settings
    public static Settings settings;
    
    static int m = 0, s = 0;
    private static Timer timer;
    public static NavMesh shipNM;
    public static Node root;
    public static Population population;
    public static int updateTimes = 0;
    public static Geometry[] AgentGeometries;
    private static mainWindow window;
    private static JmeCanvasContext ctx;
    private static int populationSize;
    private Node navMeshHolder;

    private static void createComponents() {
        //Creates a JFrame for the main window
        window = new mainWindow();
        window.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                timer.stop();
            }
        });
        // Creates JMonkey the canvas 
        Main canvasApplication = new Main();
        canvasApplication.setShowSettings(false);
        canvasApplication.createCanvas(); // create canvas!
        ctx = (JmeCanvasContext) canvasApplication.getContext();
        ctx.setSystemListener(canvasApplication);
        Dimension dim = new Dimension(800, 580);
        ctx.getCanvas().setSize(dim);
        //Sets the canvas in the panel on the JFrame
        populationSize = settings.getPopulationNumber();
        window.setContainer(ctx.getCanvas());
        window.setNumOfPeople(populationSize);
        timer = new Timer(1000, new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                s++;
                if (s == 60){
                    m++;
                    s = 0;
                }
                window.setTimeElapsed(m, s);

                if (population.isDone()) {
                    timer.stop();
                }

            }    
          });
        //Makes the window visible
        window.setVisible(true);
    }


    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //Load Settings
                settings = new Settings();
                settings.loadFromFile();
                //Creates components of GUI
                createComponents();
                window.update(settings);
            }
        });

    }

    public static Population getPopulation() {
        return population;
    }

    @Override
    public void simpleInitApp() {
        
        this.setDisplayStatView(false); //Hides debug info
        this.setDisplayFps(settings.isShowFPS()); //Depending on settings hides FPS
        
        //Sets cam mouse controls
        flyCam.setDragToRotate(true);

        inputManager.addMapping("play", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addListener(actionListener, "play");
        
        inputManager.addMapping("route", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addListener(actionListener, "route");

        //Sets Cam speed
        this.flyCam.setMoveSpeed(settings.getCamSpeed());

        Logger.getLogger("").setLevel(Level.SEVERE);

        //Loads Model
        Spatial ship = assetManager.loadModel(settings.getModelLocation());
        Node node = (Node) ship;
        Node chil1 = (Node) node.getChildren().get(0);
        Geometry chil = (Geometry) chil1.getChildren().get(0);

        Mesh shipMesh = chil.getMesh();
        shipNM = new NavMesh();

        NavMeshGenerator generator = new NavMeshGenerator();

        Mesh optimisedMesh = generator.optimize(shipMesh);

        shipNM.loadFromMesh(optimisedMesh);

        //Draws NavMesh
        if (settings.isShowNavMesh()) {
            drawNavmesh();
        }

        guiFont = assetManager.loadFont(settings.getGuiFont());
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(0.1f);
        helloText.setText("ORIGIN");
        helloText.setLocalTranslation(0, 0, 0);
        rootNode.attachChild(helloText);

        try {
            population = new Population(rootNode, shipNM, this);
        } catch (Exception e) {
        }
        PersonCategory athletes = new PersonCategory("Athletes", 2.2f, 10.0f, 5f, 7f, ColorRGBA.Blue, 5);
        PersonCategory infants = new PersonCategory("Infant", 1f, 3f, 2f, 3f, ColorRGBA.Red, 3);
        PersonCategory old = new PersonCategory("Old", 2f, 6f, 2f, 5f, ColorRGBA.Green, 8);
        PersonCategory teenager = new PersonCategory("Teenager", 2.2f, 9f, 5f, 8.2f, ColorRGBA.Gray, 10);
        population.addPersonCategoryObj(athletes);
        population.addPersonCategoryObj(infants);
        population.addPersonCategoryObj(old);
        population.addPersonCategoryObj(teenager);

        

        AgentGeometries = new Geometry[populationSize];
        population.populate(populationSize);

        //Hides Loading Text
        window.loadDone();

    }
    
    public static void updateStatus() {
        window.updateStatus(populationSize, population.getNumberOfEvacuee());
        window.update(settings);
    }
    
    public void removeNavmesh() {
        rootNode.detachChild(navMeshHolder);
    }
    public void drawNavmesh() {
        navMeshHolder = new Node();
        rootNode.attachChild(navMeshHolder);
        //draws navMesh
        for (int i = 0; i < shipNM.getNumCells(); i++) {
            Vector3f v0 = shipNM.getCell(i).getVertex(0);
            Vector3f v1 = shipNM.getCell(i).getVertex(1);
            Vector3f v2 = shipNM.getCell(i).getVertex(2);
            Vector3f[][] vs = {{v0,v1}, {v2,v0}, {v1,v2}};
            
            guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");

            for (Vector3f[] vt : vs) {
                Mesh lineMesh = new Mesh();
                lineMesh.setMode(Mesh.Mode.Lines);
                lineMesh.setBuffer(VertexBuffer.Type.Position, 3, new float[]{vt[0].x, vt[0].y, vt[0].z, vt[1].x, vt[1].y, vt[1].z});
                lineMesh.setBuffer(VertexBuffer.Type.Index, 2, new short[]{0, 1});
                lineMesh.updateBound();
                lineMesh.updateCounts();
                Geometry lineGeometry = new Geometry("line", lineMesh);
                Material lineMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                lineMaterial.setColor("Color", ColorRGBA.White);
                lineGeometry.setMaterial(lineMaterial);
                navMeshHolder.attachChild(lineGeometry);
                
                BitmapText helloText = new BitmapText(guiFont, false);
                helloText.setSize(0.05f);
                helloText.setText(vt[0].toString());
                helloText.setLocalTranslation(vt[0].x, vt[0].y, vt[0].z);
                //navMeshHolder.attachChild(helloText);
            }
        }
    }

    private static ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean pressed, float tpf) {
            if (name.equals("play") && !pressed) {
                population.play();
                timer.start();
            }
            else if (name.equals("route") && !pressed) {
                population.evacuate();
            }
        }
    };
}

class SettingDlg extends JDialog{
    
    static final int FPS_MIN = 0;
    static final int FPS_MAX = 100;
    static final int FPS_INIT = 15;
    
    private Container container;
    private ArrayList<PersonCategory> personCategories;
    private Map<String,ColorRGBA> colorMap;
    private PersonCategory selectedCategory;
    
    
    // populationPanel components
    private JTabbedPane tabbedPane;
    private JTextField txtCategory;
    private JComboBox categoryCombo, colorCombo;
    private JTable populationTable;
    private JButton addBut, removeBut;
    private JSpinner minSpeedSpinner, maxSpeedSpinner;
    private JSpinner minStressSpinner, maxStressSpinner;
    private JSpinner peopleNum;
    private JPanel generalPanel, populationPanel, distributionPanel;
    
    //distributionPanel components
    private JTable distributionTable;
    private DistributionModel model;
    
    private GridBagConstraints c;
    
    
    
    public SettingDlg(){
        personCategories = Main.getPopulation().returnCategories();
        selectedCategory = personCategories.get(0);
        colorMap = new HashMap<String,ColorRGBA>(){{
            put("Red", ColorRGBA.Red);
            put("Blue", ColorRGBA.Blue);
            put("Brown", ColorRGBA.Brown);
            put("Cyan", ColorRGBA.Cyan);
            put("Gray", ColorRGBA.Gray);
            put("Green", ColorRGBA.Green);
            put("Magenta", ColorRGBA.Magenta);
            put("Orange", ColorRGBA.Orange);
            put("Pink", ColorRGBA.Pink);
            put("White", ColorRGBA.White);
            put("Yellow", ColorRGBA.Yellow);
            
        }};
        container = this.getContentPane();
        container.setLayout(new GridLayout(1, 1));
        tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        c = new GridBagConstraints();
        
        createGeneralPanel();
        createPopulationPanel();
        createDistributionPanel();

        container.add(tabbedPane);
        this.setLocationRelativeTo(null);
        this.setTitle("Advanced Settings");
        this.setModal(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//        this.pack();
        this.setSize(500, 210);
        this.setResizable(false);
        this.setVisible(true);

    }
    
    private void createGeneralPanel(){
        generalPanel = new JPanel(new GridBagLayout());
        generalPanel.add(new JButton("test"));
        tabbedPane.addTab("General", generalPanel);
    }
    
    private void createDistributionPanel(){
        
        distributionPanel = new JPanel(new GridLayout());
        
        model = new DistributionModel(personCategories);
        distributionTable = new JTable(model);
        distributionTable.setRowHeight(40);
        distributionTable.getTableHeader().setResizingAllowed(false);
        distributionTable.getTableHeader().setReorderingAllowed(false);
        
        DistributionCellRenderer renderer = new DistributionCellRenderer();
        DistributionCellEditor editor = new DistributionCellEditor();
        TableColumn tableColumn = distributionTable.getColumnModel().getColumn(1);
        tableColumn.setCellRenderer(renderer);
        tableColumn.setCellEditor(editor);
        
        

        JScrollPane scrollPane = new JScrollPane(distributionTable);
        distributionPanel.add(scrollPane);
        
        tabbedPane.addTab("Distribution", distributionPanel);

    }
    
    private void setData(PersonCategory p){
        
        if (p != null){
            categoryCombo.removeActionListener(l);
            categoryCombo.removeAllItems();
            for (String s: Main.getPopulation().getPersonCategoryNames()){
                System.out.println(s);
                categoryCombo.addItem(s);
            }
            categoryCombo.addActionListener(l);
            selectedCategory = p;
            for (Object s: colorMap.keySet().toArray()){
                if (selectedCategory.getColor() == colorMap.get(s)){
                    colorCombo.setSelectedItem(s);
                }
            }
            peopleNum.setValue(selectedCategory.getNumberOfPeople());
            minSpeedSpinner.setValue(selectedCategory.getMinspeed());
            maxSpeedSpinner.setValue(selectedCategory.getMaxspeed());
            minStressSpinner.setValue(selectedCategory.getMinStress());
            maxStressSpinner.setValue(selectedCategory.getMaxStress());
        }else{
            selectedCategory = null;
            peopleNum.setValue(1);
            minSpeedSpinner.setValue(0d);
            maxSpeedSpinner.setValue(0d);
            minStressSpinner.setValue(0d);
            maxStressSpinner.setValue(0d);
        }
        
        
    }
    
    private void createPopulationPanel(){
        
        populationPanel = new JPanel(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(4, 0, 0, 8);
        c.anchor = c.LINE_END;
        JLabel label = new JLabel("Category: ");
        populationPanel.add(label, c);
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        categoryCombo = new JComboBox();
        categoryCombo.addActionListener(l);
        populationPanel.add(categoryCombo, c);
        
        c.gridx = 2;
        c.fill = GridBagConstraints.NONE;
        removeBut = new JButton("Remove");
        removeBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                personCategories.remove(selectedCategory);
                if (personCategories.size() > 0){
                    setData(personCategories.get(0));
                }else{
                    setData(null);
                }
            }
        });
        populationPanel.add(removeBut, c);
        
        c.gridx = 0;
        c.gridy = 1;
        label = new JLabel("Color: ");
        populationPanel.add(label, c);
        
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        colorCombo = new JComboBox(colorMap.keySet().toArray());
        colorCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ColorRGBA color = colorMap.get(colorCombo.getSelectedItem());
                selectedCategory.setColor(color);
            }
        });
        
        populationPanel.add(colorCombo, c);
        
        c.gridx = 0;
        c.gridy = 2;
        label = new JLabel("Number of people: ");
        populationPanel.add(label, c);
        
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        peopleNum = new JSpinner(new SpinnerNumberModel(selectedCategory.getNumberOfPeople(), 1, 50, 1));
        peopleNum.addChangeListener(new javax.swing.event.ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                int numOfPeople = (Integer)peopleNum.getValue();
                selectedCategory.setNumberOfPeople(numOfPeople);
            }

        });
        
        populationPanel.add(peopleNum, c);
        
        
        c.gridx = 0;
        c.gridy = 3;
        c.fill = GridBagConstraints.LINE_END;
        label = new JLabel("Speed(min-max): ");
        populationPanel.add(label, c);
        
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        minSpeedSpinner = new JSpinner(new SpinnerNumberModel(selectedCategory.getMinspeed(),
                                                    0.0f, 10.0f, 0.1f));
        minSpeedSpinner.addChangeListener(new javax.swing.event.ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                double minSpeed = (Double)minSpeedSpinner.getValue();
                selectedCategory.setMinspeed(minSpeed);
            }

        });
        
        populationPanel.add(minSpeedSpinner, c);
        
        c.gridx = 2;
        maxSpeedSpinner = new JSpinner(new SpinnerNumberModel(selectedCategory.getMaxspeed(),
                                                        0.0f, 10.0f, 0.1f));
        maxSpeedSpinner.addChangeListener(new javax.swing.event.ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                double maxSpeed = (Double)maxSpeedSpinner.getValue();
                selectedCategory.setMaxspeed(maxSpeed);
            }

        });
        
        populationPanel.add(maxSpeedSpinner, c);
        
        c.gridx = 0;
        c.gridy = 4;
        c.fill = GridBagConstraints.LINE_END;
        
        label = new JLabel("Stress(min-max): ");
        populationPanel.add(label, c);
        
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        minStressSpinner = new JSpinner(new SpinnerNumberModel(selectedCategory.getMinStress(),
                                                    0.0f, 10.0f, 0.1f));
        minStressSpinner.addChangeListener(new javax.swing.event.ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                double minStress = (Double)minStressSpinner.getValue();
                selectedCategory.setMinStress(minStress);
            }

        });
        
        populationPanel.add(minStressSpinner, c);
        
        c.gridx = 2;
        maxStressSpinner = new JSpinner(new SpinnerNumberModel(selectedCategory.getMaxStress(),
                                                        0.0f, 10.0f, 0.1f));
        maxStressSpinner.addChangeListener(new javax.swing.event.ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                double maxStress = (Double)maxStressSpinner.getValue();
                selectedCategory.setMaxStress(maxStress);
            }

        });
        
        populationPanel.add(maxStressSpinner, c);
        
        c.gridx = 0;
        c.gridy = 5;
        
        c.insets = new Insets(10, 0, 0, 0);
        txtCategory = new JTextField(5);
        populationPanel.add(new JSeparator(SwingConstants.HORIZONTAL), c);
        c.gridx = 1;
        populationPanel.add(new JSeparator(SwingConstants.HORIZONTAL), c);
        c.gridx = 2;
        populationPanel.add(new JSeparator(SwingConstants.HORIZONTAL), c);   
        c.insets = new Insets(4, 0, 5, 8);
        c.gridx = 0;
        c.gridy = 6;
        c.fill = GridBagConstraints.NONE;
        label = new JLabel("New Category: ");
        populationPanel.add(label, c);
        
        
        c.gridx = 1;
        c.gridy = 6;
        c.fill = GridBagConstraints.HORIZONTAL;
        populationPanel.add(txtCategory, c);
        
        c.gridx = 2;
        addBut = new JButton("Add");
        addBut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (txtCategory.getText().equals("")){
                    return ;
                }
                String categoryName = txtCategory.getText();                
                PersonCategory p = new PersonCategory(categoryName, 0d, 0d, 0d, 0d, ColorRGBA.Blue, 1);
                personCategories.add(0, p);
                setData(p);
                txtCategory.setText("");
            }
        });
        populationPanel.add(addBut, c);
        
        setData(selectedCategory);

        tabbedPane.addTab("Population", populationPanel);
    }
    
    private java.awt.event.ActionListener l = new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            for (PersonCategory p: personCategories){
                if (p.getName().equals(categoryCombo.getSelectedItem())){
                    setData(p);
                }
            }
        }
    };
    
}

class DistributionCellEditor implements TableCellEditor {
    
    static final int FPS_MIN = 0;
    static final int FPS_MAX = 100;
    
    private JSlider slider;
        
	
    public DistributionCellEditor() {
        slider = new JSlider(JSlider.HORIZONTAL);
        slider.setMinimum(FPS_MIN);
        slider.setMaximum(FPS_MAX);
        slider.setMajorTickSpacing(20);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
    }

    public Object getCellEditorValue() {
        return slider.getValue();
    }
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }
    public boolean stopCellEditing() {
        return true;
    }
    public void cancelCellEditing() {

    }
    public void addCellEditorListener(CellEditorListener l) {

    }
    public void removeCellEditorListener(CellEditorListener l) {
    }
    public Component getTableCellEditorComponent(JTable table, Object value,
                    boolean isSelected, int row, int column) {
        slider = new JSlider(JSlider.HORIZONTAL);
        slider.setMinimum(FPS_MIN);
        slider.setMaximum(FPS_MAX);
        slider.setMajorTickSpacing(20);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setValue((Integer)value);
        
        return slider;
    }
}

class DistributionCellRenderer implements TableCellRenderer {

    static final int FPS_MIN = 0;
    static final int FPS_MAX = 100;
    
    public JSlider slider;
    


    public DistributionCellRenderer() {
        slider = new JSlider(JSlider.HORIZONTAL);
        slider.setMinimum(FPS_MIN);
        slider.setMaximum(FPS_MAX);
        slider.setMajorTickSpacing(20);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean selected, boolean focused, int row, int column) {
        slider.setValue((Integer)value);
        
        return slider;
    }

}

class DistributionModel extends AbstractTableModel {
	public ArrayList<PersonCategory> data;
	private static final String COL_NAMES[] = {"Category", "%"};

	public DistributionModel(ArrayList<PersonCategory> arr) {
            data = arr;
	}

	public int getColumnCount() {
            return COL_NAMES.length;
	}

	public String getColumnName(int col) {
            return COL_NAMES[col];
	}

	public int getRowCount() {
            return data.size();
	}

	public Object getValueAt(int nRow, int nCol) {
            PersonCategory row = (PersonCategory) data.get(nRow);
            switch(nCol){
                case 0:
                    return row.getName();
                case 1:
                    return row.getNumberOfPeople();
            }
            return "";
	}

	public boolean isCellEditable(int row, int col) {
            if (col == 1){
		return true;
            }
            return false;
	}

	public void setValueAt(Object value, int nRow, int nCol) {
            if (nRow < 0 || nRow >= getRowCount() || value == null)
                return;
//            PersonCategory row = (PersonCategory) data.get(nRow);
//            PersonCategory newData = (PersonCategory)value;
//            row.value = (Integer)newData.value;
//            fireTableCellUpdated(nRow, nCol);
	}
}
