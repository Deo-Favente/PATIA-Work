(define (problem sokoban2)
	; #####	
	; #@  #
    ; # $$#
    ; # ..#
	; ##### 
    
  (:domain sokoban)

  (:objects
    empty1 empty2 empty3 empty4 empty5 empty6 empty7
    goal1 goal2  
    - case
  )

  (:init
    ;; Worker
    (workerOn empty1)

    ;; Boxes
    (boxOn empty5)
    (boxOn empty6)

    ;; Cases claires
    (clear empty2)
    (clear empty3)
    (clear empty4)
    (clear empty7)
    (clear goal1)
    (clear goal2)

    ;; Connexions horizontales
    (nextRight empty1 empty2)
    (nextRight empty2 empty3)
    (nextLeft empty2 empty1)
    (nextLeft empty3 empty2)

    (nextRight empty4 empty5)
    (nextRight empty5 empty6)
    (nextLeft empty5 empty4)
    (nextLeft empty6 empty5)

    (nextRight goal1 goal2)
    (nextLeft goal2 goal1)

    ;; Connexions verticales
    (nextDown empty1 empty4)
    (nextDown empty2 empty5)
    (nextDown empty3 empty6)
    (nextUp empty4 empty1)
    (nextUp empty5 empty2)
    (nextUp empty6 empty3)

    (nextDown empty4 goal1)
    (nextDown empty5 goal2)
    (nextUp goal1 empty4)
    (nextUp goal2 empty5)
  )

  (:goal
    (and
      (boxOn goal1)
      (boxOn goal2)
    )
  )
)