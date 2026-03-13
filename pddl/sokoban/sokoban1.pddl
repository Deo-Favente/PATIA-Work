(define (problem sokoban1)
    ; # is a wall 
    ; $ is a box 
    ; . is a destination 
    ; * is a box on a storage place
    ; @ is the warehouse guard 
    ; + is this guard on a storage place 
    ; a space character is the floor

	; #####	w1-w2-w3-w4-w5
	; #@$.#	w6-e1-e2-g1-w7
	; ##### w8-w9-w10-w11-w12

	(:domain sokoban)
	(:objects
    	wall1 wall2 wall3 wall4 wall5 wall6 wall7 wall8 wall9 wall10 wall11 wall12 empty1 empty2 goal1 - case
	)

	(:init
		(clear goal1)
        (workerOn empty1)
		(boxOn empty2)
		(target goal1)

		(nextDown wall1 wall6)
		(nextRight wall1 wall2)

		(nextLeft wall2 wall1)
		(nextDown wall2 empty1)
		(nextRight wall2 wall3)

		(nextLeft wall3 wall2)
		(nextDown wall3 empty2)
		(nextRight wall3 wall4)

		(nextLeft wall4 wall3)
		(nextDown wall4 goal1)
		(nextRight wall4 wall5)

		(nextLeft wall5 wall4)
		(nextDown wall5 wall7)

		(nextUp wall6 wall1)
		(nextRight wall6 empty1)
		(nextDown wall6 wall8)

		(nextUp empty1 wall2)
		(nextLeft empty1 wall6)
		(nextRight empty1 empty2)
		(nextDown empty1 wall9)
		
		(nextUp empty2 wall3)
		(nextLeft empty2 empty1)
		(nextRight empty2 goal1)
		(nextDown empty2 wall10)
		
		(nextUp goal1 wall4)
		(nextLeft goal1 empty2)
		(nextRight goal1 wall7)
		(nextDown goal1 wall11)

		(nextUp wall7 wall5)
		(nextLeft wall7 empty2)
		(nextDown wall7 wall12)

		(nextUp wall8 wall6)
		(nextRight wall8 wall9)
		
		(nextUp wall9 empty1)
		(nextLeft wall9 wall8)
		(nextRight wall9 wall10)

		(nextUp wall10 empty2)
		(nextLeft wall10 wall9)
		(nextRight wall10 wall11)

		(nextUp wall11 goal1)
		(nextLeft wall11 wall10)
		(nextRight wall11 wall12)

		(nextUp wall12 wall7)
		(nextLeft wall12 wall11)
		
		; #####	w1-w2-w3-w4-w5
		; #@$.#	w6-e1-e2-g1-w7
		; ##### w8-w9-w10-w11-w12
	)

	(:goal
		(boxOn goal1)
	)
)