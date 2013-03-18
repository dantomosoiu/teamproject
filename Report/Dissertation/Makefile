

teamL_dissertation_2013.pdf: realclean teamL_dissertation_2013.tex teamL_dissertation_2013.bib
	pdflatex teamL_dissertation_2013
	bibtex teamL_dissertation_2013
	pdflatex teamL_dissertation_2013
	pdflatex teamL_dissertation_2013

clean:
	rm -f *.log *.aux *.toc *.out
	rm -f *.bbl *.blg

realclean:
	rm -f *.log *.aux *.toc *.out
	rm -f *.bbl *.blg
	rm -f teamL_dissertation_2013.pdf
	
