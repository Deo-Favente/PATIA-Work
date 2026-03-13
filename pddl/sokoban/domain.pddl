(define (domain sokoban)
    (:requirements :strips :typing)
    (:types case)

    (:predicates
        (clear ?c - case)
        (boxOn ?t - case)
        (workerOn ?c - case)
        (nextUp ?c1 - case ?c2 - case)
        (nextRight ?c1 - case ?c2 - case)
        (nextDown ?c1 - case ?c2 - case)
        (nextLeft ?c1 - case ?c2 - case)
    )

    (:action move_worker_up
    :parameters (?from - case ?to - case)
    :precondition (and (workerOn ?from) (nextUp ?from ?to) (clear ?to))
    :effect (and (not (workerOn ?from)) (workerOn ?to) (clear ?from) (not (clear ?to)))
    )

    (:action move_worker_down
    :parameters (?from - case ?to - case)
    :precondition (and (workerOn ?from) (nextDown ?from ?to) (clear ?to))
    :effect (and (not (workerOn ?from)) (workerOn ?to) (clear ?from) (not (clear ?to)))
    )

    (:action move_worker_left
    :parameters (?from - case ?to - case)
    :precondition (and (workerOn ?from) (nextLeft ?from ?to) (clear ?to))
    :effect (and (not (workerOn ?from)) (workerOn ?to) (clear ?from) (not (clear ?to)))
    )

    (:action move_worker_right
    :parameters (?from - case ?to - case)
    :precondition (and (workerOn ?from) (nextRight ?from ?to) (clear ?to))
    :effect (and (not (workerOn ?from)) (workerOn ?to) (clear ?from) (not (clear ?to)))
    )

    (:action move_box_up
    :parameters (?w - case ?box - case ?to - case)
    :precondition (and (workerOn ?w) (boxOn ?box) (clear ?to) (nextUp ?w ?box) (nextUp ?box ?to))
    :effect (and (not (workerOn ?w)) (workerOn ?box) (not (boxOn ?box)) (boxOn ?to) (clear ?w) (not (clear ?to)))
    )

    (:action move_box_down
    :parameters (?w - case ?box - case ?to - case)
    :precondition (and (workerOn ?w) (boxOn ?box) (clear ?to) (nextDown ?w ?box) (nextDown ?box ?to))
    :effect (and (not (workerOn ?w)) (workerOn ?box) (not (boxOn ?box)) (boxOn ?to) (clear ?w) (not (clear ?to)))
    )

    (:action move_box_left
    :parameters (?w - case ?box - case ?to - case)
    :precondition (and (workerOn ?w) (boxOn ?box) (clear ?to) (nextLeft ?w ?box) (nextLeft ?box ?to))
    :effect (and (not (workerOn ?w)) (workerOn ?box) (not (boxOn ?box)) (boxOn ?to) (clear ?w) (not (clear ?to)))
    )

    (:action move_box_right
    :parameters (?w - case ?box - case ?to - case)
    :precondition (and (workerOn ?w) (boxOn ?box) (clear ?to) (nextRight ?w ?box) (nextRight ?box ?to))
    :effect (and (not (workerOn ?w)) (workerOn ?box) (not (boxOn ?box)) (boxOn ?to) (clear ?w) (not (clear ?to)))
    )
)