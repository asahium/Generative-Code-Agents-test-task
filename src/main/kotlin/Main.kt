import org.json.JSONArray
import org.json.JSONException
import java.io.File

fun main(args: Array<String>) {
    if (args.size < 2) {
        println("Usage: <program> <path to python file> <number of attempts>")
        return
    }
    val correctedFilePath = "correct.py"
    val pythonFilePath = args[0] // default "./src/data/Many.py"
    val maxAttempts = args[1].toIntOrNull() ?: 10
    val modelName = args[2]
    var attempt = 0
    var hasErrors: Boolean


    println("Starting correction process")
    do {
        sendToLLM(pythonFilePath, modelName)

        try {
            val jsonData = File("answer.json").readText()
            val jsonArray = JSONArray(jsonData)
            val generatedText = jsonArray
                .getJSONObject(0)
                .getString("generated_text")

            val correctedCode = generatedText
                .split("\n")
                .drop(2)
                .filterNot { line: String -> line.startsWith('\'') || line.startsWith('\"') || line.startsWith('`') }
                .joinToString("\n")

            hasErrors = correctedCode.isNotEmpty() && correctedCode != File(correctedFilePath).readText()
            if (hasErrors) {
                File(correctedFilePath).writeText(correctedCode)
                hasErrors = runPythonScript(correctedFilePath)
            }

        } catch (e: JSONException) {
            println("Error parsing JSON data: ${e.message}")
            hasErrors = true
        } catch (e: Exception) {
            println("An error occurred: ${e.message}")
            hasErrors = true
        }

        attempt++
    } while (hasErrors && attempt < maxAttempts)

    if (hasErrors) {
        println("Could not fix all errors within 10 attempts.")
    } else {
        println("Correction successful. No errors detected in the corrected code. File: $correctedFilePath")
    }
}

fun sendToLLM(filePath: String, modelName: String) {
    try {
        val processBuilder = ProcessBuilder("./request.sh", filePath, modelName)
        processBuilder.redirectErrorStream(true)
        val process = processBuilder.start()
        process.inputStream.bufferedReader().use {
            println(it.readText())
        }
        process.waitFor()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun runPythonScript(filePath: String): Boolean {
    try {
        val processBuilder = ProcessBuilder("python3", filePath)
        processBuilder.redirectErrorStream(true)
        val process = processBuilder.start()

        val output = process.inputStream.bufferedReader().readText()
        process.waitFor()

        if (output.isNotEmpty()) {
            println("Python execution output or error: $output")
            return true
        }
        return false
    } catch (e: Exception) {
        e.printStackTrace()
        return true
    }
}
