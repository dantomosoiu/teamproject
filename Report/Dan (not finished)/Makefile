

teamL_dissertation_2013.pdf: import\ and\ navmesh.tex import\ and\ navmesh.bib
	pdflatex import\ and\ navmesh
	bibtex import\ and\ navmesh.bib
	pdflatex import\ and\ navmesh
	pdflatex import\ and\ navmesh

clean:
	rm -f *.log *.aux *.toc *.out
	rm -f *.bbl *.blg

realclean:
	rm -f *.log *.aux *.toc *.out
	rm -f *.bbl *.blg
	rm -f teamL_dissertation_2013.pdf
	
