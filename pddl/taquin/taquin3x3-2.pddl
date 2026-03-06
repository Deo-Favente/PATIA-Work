(define (problem taquin-3x3-2)
	(:domain taquin)
	(:objects
    	cube1 cube2 cube3 cube4 cube5 cube6 cube7 cube8 - cube
		case1 case2 case3 case4 case5 case6 case7 case8 case9 - case)

	(:init
		; adjacences des cases
		(next case1 case2) (next case1 case4)
		(next case2 case1) (next case2 case3) (next case2 case5)
		(next case3 case2) (next case3 case6)
		(next case4 case1) (next case4 case5) (next case4 case7)
		(next case5 case2) (next case5 case4) (next case5 case6) (next case5 case8)
		(next case6 case3) (next case6 case5) (next case6 case9)
		(next case7 case4) (next case7 case8)
		(next case8 case5) (next case8 case7) (next case8 case9)
		(next case9 case6) (next case9 case8)

		; placement des cubes sur les cases
		(on cube7 case1)
		(on cube4 case2)
		(on cube2 case3)
		(on cube8 case4)
		(on cube5 case5)
		(on cube6 case6)
		(on cube1 case7)
		(on cube3 case8)

		(clear case9)
	)

	(:goal
		(and
			(on cube1 case2)
			(on cube2 case3)
			(on cube3 case4)
			(on cube4 case5)
			(on cube5 case6)
			(on cube6 case7)
			(on cube7 case8)
			(on cube8 case9)
			(clear case1)
		)
	)
)