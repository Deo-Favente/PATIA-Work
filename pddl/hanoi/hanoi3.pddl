(define (problem hanoi-2)
	(:domain hanoi)
	(:objects
    	p1 p2 p3 d1 d2 d3 - disk)
	(:init
		(smaller d1 p1) (smaller d1 p2) (smaller d1 p3)
		(smaller d2 p1) (smaller d2 p2) (smaller d2 p3)
		(smaller d3 p1) (smaller d3 p2) (smaller d3 p3)
		(on d3 p1) (on d2 d3) (on d1 d2) (clear p2) (clear p3)
		(clear d1) (smaller d1 d2) (smaller d2 d3) (smaller d1 d3)
	)
	(:goal 
		(and (on d3 p3) (on d2 d3) (on d1 d2))
	)
)

