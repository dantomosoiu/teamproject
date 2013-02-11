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
import java.awt.Insets;

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
/**
 *
 */
public class Main extends SimpleApplication {
    
    //Settings
    public static Settings set;
    
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
        populationSize = set.getPopulationNumber();
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
                set = new Settings();
                set.loadFromFile();
                //Creates components of GUI
                createComponents();
                window.update(set);
            }
        });

    }

    public static Population getPopulation() {
        return population;
    }

    @Override
    public void simpleInitApp() {
        
        this.setDisplayStatView(false); //Hides debug info
        this.setDisplayFps(set.isShowFPS()); //Depending on settings hides FPS
        
        //Sets cam mouse controls
        flyCam.setDragToRotate(true);

        inputManager.addMapping("play", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addListener(actionListener, "play");
        
        inputManager.addMapping("route", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addListener(actionListener, "route");

        //Sets Cam speed
        this.flyCam.setMoveSpeed(set.getCamSpeed());

        Logger.getLogger("").setLevel(Level.SEVERE);

        //Loads Model
        Spatial ship = assetManager.loadModel(set.getModelLocation());
        Node node = (Node) ship;
        Node chil1 = (Node) node.getChildren().get(0);
        Geometry chil = (Geometry) chil1.getChildren().get(0);

        Mesh shipMesh = chil.getMesh();
        shipNM = new NavMesh();

        NavMeshGenerator generator = new NavMeshGenerator();

        Mesh optimisedMesh = generator.optimize(shipMesh);

        shipNM.loadFromMesh(optimisedMesh);

        //Draws NavMesh
        if (set.isShowNavMesh()) {
            drawNavmesh();
        }

        guiFont = assetManager.loadFont(set.getGuiFont());
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(0.1f);
        helloText.setText("ORIGIN");
        helloText.setLocalTranslation(0, 0, 0);
        rootNode.attachChild(helloText);

        try {
            population = new Population(rootNode, shipNM, this);
        } catch (Exception e) {
        }

        

        AgentGeometries = new Geometry[populationSize];
        population.populate(populationSize);

        //Hides Loading Text
        window.loadDone();

    }
    
    public static void updateStatus() {
        window.updateStatus(populationSize, population.getNumberOfEvacuee());
        window.update(set);
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
    
    // populationPanel components
    private JTabbedPane tabbedPane;
    private JTextField txtCategory;
    private JComboBox categoryCombo;
    private JButton addBut, removeBut;
    private JSpinner speedSpinner, stressSpinner, spaceSpinner, prioritySpinner;
    private JPanel generalPanel, populationPanel, distributionPanel;
    
    //distributionPanel components
    private JSlider sldDisabled, sldInfant, sldAthlete;
    private JTable table;
    private DistributionModel model;
    
    private GridBagConstraints c;
    
    
    
    public SettingDlg(){
        
        container = this.getContentPane();
        container.setLayout(new GridLayout(1, 1));
        tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        c = new GridBagConstraints();
        
        createGeneralPanel();
        createPopulationPanel();
        createDistributionPanel();

        container.add(tabbedPane);
//        this.setLocationRelativeTo(null);
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
        
        model = new DistributionModel();
        table = new JTable(model);
        table.setRowHeight(40);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        
        DistributionCellRenderer renderer = new DistributionCellRenderer();
        DistributionCellEditor editor = new DistributionCellEditor();
        TableColumn tableColumn = table.getColumnModel().getColumn(1);
        tableColumn.setCellRenderer(renderer);
        tableColumn.setCellEditor(editor);
        
        

        JScrollPane scrollPane = new JScrollPane(table);
        distributionPanel.add(scrollPane);
        
        tabbedPane.addTab("Distribution", distributionPanel);

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
        categoryCombo = new JComboBox(new Object[]{"Atheletes", "test2asdfasfsdfewr", "test3"});
        populationPanel.add(categoryCombo, c);
        
        c.gridx = 2;
        removeBut = new JButton("Remove");
        populationPanel.add(removeBut, c);
        
        c.gridx = 0;
        c.gridy = 1;
        label = new JLabel("Speed: ");
        populationPanel.add(label, c);
        
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        float value = 0.0f;
        float min = 0.0f;
        float max = 10.0f;
        float step = 0.1f;
        SpinnerNumberModel speedModel = new SpinnerNumberModel(value, min, max, step);
        speedSpinner = new JSpinner(speedModel);
        populationPanel.add(speedSpinner, c);
        
        c.gridx = 0;
        c.gridy = 2;
        c.fill = GridBagConstraints.NONE;
        label = new JLabel("Initial Stress: ");
        populationPanel.add(label, c);
        
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        SpinnerNumberModel stressModel = new SpinnerNumberModel(value, min, 100.0f, step);
        stressSpinner = new JSpinner(stressModel);
        populationPanel.add(stressSpinner, c);
        
        c.gridx = 0;
        c.gridy = 3;
        c.fill = GridBagConstraints.NONE;
        label = new JLabel("Personal Space(m.): ");
        populationPanel.add(label, c);
        
        c.gridx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        SpinnerNumberModel spaceModel = new SpinnerNumberModel(value, min, 5.0f, step);
        spaceSpinner = new JSpinner(spaceModel);
        populationPanel.add(spaceSpinner, c);
        
        c.gridx = 0;
        c.gridy = 4;
        c.fill = GridBagConstraints.NONE;
        label = new JLabel("Social Priority: ");
        populationPanel.add(label, c);
        
        c.gridx = 1;
        c.gridy = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        SpinnerNumberModel priorityModel = new SpinnerNumberModel(value, min, 1.0f, step);
        prioritySpinner = new JSpinner(priorityModel);
        populationPanel.add(prioritySpinner, c);
        
        c.gridx = 0;
        c.gridy = 5;
        c.insets = new Insets(10, 0, 0, 0);
        txtCategory = new JTextField(10);
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
        populationPanel.add(addBut, c);

        tabbedPane.addTab("Population", populationPanel);
    }
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

class Data{
    
    protected String category;
    protected int value;
    
    public Data(String cat, int val){
        this.category = cat;
        this.value = val;
    }
}

class DistributionModel extends AbstractTableModel {
	public ArrayList<Data> data;
	private static final String COL_NAMES[] = {"Category", "Value"};

	public DistributionModel() {
            data = new ArrayList<Data>();
            setData();
	}
	
	public void setData(){
            data.add(new Data("Disabled", 20));
            data.add(new Data("Infant", 10));
            data.add(new Data("Old", 40));
            data.add(new Data("Atheletes", 90));
            data.add(new Data("Teenager", 90));
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
            Data row = (Data) data.get(nRow);
            switch(nCol){
                case 0:
                    return row.category;
                case 1:
                    return row.value;
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
            Data row = (Data) data.get(nRow);
            Data newData = (Data)value;
            row.category = (String)newData.category;
            row.value = (Integer)newData.value;
            fireTableCellUpdated(nRow, nCol);
	}

}