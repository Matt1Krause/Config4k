package io.config4k.annotation

/**
 * Specifies the name of a property as the single components.
 * If you want your property to be named 'someName' then you need to use PropertyName("some", "name")
 *
 * @see Properties
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.BINARY)
annotation class PropertyName(vararg val nameElements: String)
