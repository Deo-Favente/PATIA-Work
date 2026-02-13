(define (domain hanoi)
    (:requirements :strips :typing)
    (:types disk)

    (:predicates 
    	(on ?d - disk ?t - disk)
		(clear ?x)
		(smaller ?d1 - disk ?d2 - disk)
    )

    (:action move
    :parameters (?d - disk ?from - disk ?to - disk)
    :precondition (and (on ?d ?from) (smaller ?to ?d) (clear ?d) (clear ?to))
    :effect (and (not (on ?d ?from)) (clear ?from) (not (clear ?to)) (on ?d ?to))
    )
)