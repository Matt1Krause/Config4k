import java.nio.file.Files
import java.nio.file.Path
import java.util.*

fun main() {
    val p = Properties()
    p.load(Files.newInputStream(Path.of("property-format", "src", "jvmMain", "resources", "test-properties.properties")))
    p.entries.forEach { println("key: <${it.key}>, value: <${it.value}>") }
    
}