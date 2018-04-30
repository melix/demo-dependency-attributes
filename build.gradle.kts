import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

plugins {
    `java-library`
}

val statusAttribute = attribute<String>("org.gradle.status")
val qualityAttribute = attribute<String>("quality")

val all by cliOption()
val forceA by cliOption()
val forceB by cliOption()
val quality by cliOption()

configurations.compileClasspath.attributes {
    if (all != null) {
        attribute(statusAttribute, all!!)
    }
    if (quality != null) {
        attribute(qualityAttribute, quality!!)
    }
}

dependencies {
    implementation("com.acme:testA:[1,)") {
        if (forceA != null) {
            attributes.attribute(statusAttribute, forceA!!)
        }
    }
    implementation("com.acme:testB:[1,)") {
        if (forceB != null) {
            attributes.attribute(statusAttribute, forceB!!)
        }
    }
}

// Below is just helpers for the sake of the demo

tasks.create("resolveDependencies") {
    doLast {
        println("Asking for modules with status=${all ?: "any"} and quality=${quality ?: "any"}}")
        if (forceA != null) {
            println("And forcing testA to have status ${forceA}")
        }
        if (forceB != null) {
            println("And forcing testB to have status ${forceB}")
        }
        val resolutionResult = configurations.compileClasspath.incoming.resolutionResult
        resolutionResult.allComponents {
            if (id is ModuleComponentIdentifier) {
                println("Resolved variant $this with attributes ${variant.attributes}")
            }
        }
        resolutionResult.allDependencies {
            if (this is UnresolvedDependencyResult) {
                println("Unresolved dependency : $this")
            }
        }
    }
}

inline fun <reified T> attribute(name: String): Attribute<T> = Attribute.of(name, T::class.java)

fun Project.option(name: String) = findProperty(name) as String?

fun cliOption(): ReadOnlyProperty<Project, String?> = object : ReadOnlyProperty<Project, String?> {
    override
    fun getValue(thisRef: Project, property: KProperty<*>): String? =
            thisRef.findProperty(property.name) as String?
}

defaultTasks("resolveDependencies")

apply {
    from("repositories.gradle.kts")
    from("list-versions.gradle.kts")
}

