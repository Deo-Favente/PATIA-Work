(define (problem taquin-2x2)
	(:domain taquin)
	(:objects
    	cube1 - cube
		case1 case2 - case)
	(:init
      	(next case1 case2)

	  	(next case2 case1)

		(clear case2)
	  	(on cube1 case1)

	  
	)
	(:goal 
		(and (on cube1 case2) (clear case1))
	)
)

