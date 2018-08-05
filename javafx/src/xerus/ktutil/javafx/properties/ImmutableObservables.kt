package xerus.ktutil.javafx.properties

import javafx.beans.InvalidationListener
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableListBase

class ImmutableObservableList<T>(vararg val items: T) : ObservableListBase<T>() {
	
	override val size: Int
		get() = items.size
	
	override fun get(index: Int) = items[index]
	
}

class ImmutableObservable<T>(private val value: T) : ObservableValue<T> {
	override fun getValue() = value
	override fun removeListener(listener: ChangeListener<in T>?) {}
	override fun removeListener(listener: InvalidationListener?) {}
	override fun addListener(listener: ChangeListener<in T>?) {}
	override fun addListener(listener: InvalidationListener?) {}
}
