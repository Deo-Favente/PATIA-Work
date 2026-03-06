(define (domain taquin)
    (:requirements :strips :typing :disjunctive-preconditions)
    (:types case cube)

    (:predicates
        (clear ?c - case)
        (on ?c - cube ?t - case)
        (next ?c1 - case ?c2 - case)
    )

    (:action move
    :parameters (?cube - cube ?from - case ?to - case)
    :precondition (and (clear ?to) (on ?cube ?from) (or (next ?from ?to) (next ?to ?from)))
    :effect (and (not (clear ?to)) (clear ?from) (not (on ?cube ?from)) (on ?cube ?to))
    )
)