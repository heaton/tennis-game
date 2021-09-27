package me.heaton.es

typealias State = Int
typealias NewState = State
typealias Event = String

interface GenericService {
    val initialState: State
    fun businessLogic(currentState: State, event: Event): NewState

    fun handleEvent(event: Event)
    fun getState(): State
}

abstract class TraditionalService : GenericService {
    private var currentState = initialState

    override fun handleEvent(event: Event) {
        currentState = businessLogic(currentState, event)
    }

    override fun getState() = currentState
}

abstract class EventSourcingService : GenericService {
    private val events = mutableListOf<Event>()

    override fun handleEvent(event: Event) {
        events.add(event)
    }

    override fun getState(): State = events.fold(initialState, ::businessLogic)
}