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
		empty1 empty2 
		goal1 - case
	)

	(:init
		(clear goal1)
        (workerOn empty1)
		(boxOn empty2)

		(nextRight empty1 empty2)
		(nextLeft empty2 empty1)
		(nextRight empty2 goal1)
		(nextLeft goal1 empty2)
	)

	(:goal
		(boxOn goal1)
	)
)