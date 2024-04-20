# CLI Tool for Python Correction

## Description

CLI application written in Kotlin taking a Python file with some errors and applies LLM until the code runs successfully.

## Installation

To install, follow these steps:

1. Clone the repository:

   ```bash
   git clone https://github.com/asahium/Generative-Code-Agents-test-task/tree/main
   ```

2. Navigate to the project directory:

   ```bash
   cd [project directory]
   ```

3. Compile the Kotlin application:

   ```bash
   ./gradlew build
   ```

## Usage

**Be sure to insert your personal access token from [Hugging Face](https://huggingface.co/docs/hub/en/security-tokens) into `request.sh`.**

To run the tool use the following command.

### Command Syntax

```bash
./gradlew run --args="[FilePath] [MaxNum] [ModelName]"
```

### Parameters

- **FilePath**: The path to the Python script that you want to correct. Defaults to "./src/data/Many.py".
- **MaxNum**: The maximum number of iterations the tool will attempt to correct the script. Defaults to 10.
- **ModelName**: Specifies the large language model to use. Can be either 'Mixtral' or 'CodeLlama', with 'Mixtral' as the recommended and default option.

### Example

To correct a script located at "./src/data/Many.py" using Mixtral with up to 5 correction attempts:

```bash
./gradlew run --args="./src/data/Many.py 5 Mixtral"
```

### Output

The command executes until the Python script runs without errors or the maximum number of iterations is reached. If the script runs successfully without errors, it is automatically saved as ‘correct.py’ in the project’s root directory.

## Contact

For any questions contact @asahium on Telegram.

## Models Overview

Several open-source models, all hosted on HuggingFace were tried with different degrees of success.

## **Successful Models**

### Mixtral-8x7B

🔗 https://huggingface.co/mistralai/Mixtral-8x7B-Instruct-v0.1

Out of all models tried Mixtral-8x7B has demonstrated the highest efficiency in correcting errors.

When processed by Mixtral-8x7B using the specific prompt to "Fix all the errors in the python code, give only correct code as output," it efficiently corrects syntax and structure to produce error-free code.

**Original Code:**

```python
def greet(name)
    print("Hello, " + name + "!")

def calculate_area(radius):
return 3.14 * radius ** 2

numbers = [1, 2, 3, 4
for number in numbers
print(number)
```

**Corrected Code by Mixtral-8x7B:**

```python
def greet(name):
    print("Hello, " + name + "!")

def calculate_area(radius):
    return 3.14 * radius ** 2

numbers = [1, 2, 3, 4]
for number in numbers:
    print(number)
```

If not limited by API constraints, there would be opportunities to fine-tune Mixtral-8x7B specifically for fixing Python code.

### Code Llama

🔗 https://huggingface.co/codellama/CodeLlama-34b-Instruct-hf

This model is designed specifically for code synthesis and understanding. It was as successful as Mixtral-8x7B, but differs in that it also generates some explanatory text alongside the corrected code given the same prompt.

## **Models with limited success**

The next models were not included in the tool, as they showed limited success.

### CodeT5 (large-size model 770M)

🔗 https://huggingface.co/Salesforce/codet5-large

When tasked with the same error-correction prompt used for other models, CodeT5 unfortunately produces subpar outputs that often include nonsensical or irrelevant code fragments, such as:

```python
[{"generated_text":"  # def)\\n\\ndef get_name(name)\\n   "}]
```

### Codeparrot

🔗 https://huggingface.co/codeparrot/codeparrot

The next three models—Codeparrot, Stable-code-3b, and Phi-1—are constrained by the small model context lengths available via API. If deployed locally, increasing the context length may enhance their capabilities, but that possibility is limited by the task.

```markdown
Input length of input_ids is 89, but `max_length` is set to 50.
This can lead to unexpected behavior.
You should consider increasing `max_length` or, better yet, setting `max_new_tokens`.
```

### stable-code-3b

🔗 [https://huggingface.co/stabilityai/stable-code-3b](https://huggingface.co/stabilityai/stable-code-3b?text=My+name+is+Teven+and+I+am)

```markdown
Input length of input_ids is 90, but `max_length` is set to 20. This can lead to unexpected behavior. You should consider increasing `max_length` or, better yet, setting `max_new_tokens`.","warnings":["Setting `pad_token_id` to `eos_token_id`:0 for open-end generation.","There was an inference error: Input length of input_ids is 90, but `max_length` is set to 20. This can lead to unexpected behavior. You should consider increasing `max_length` or, better yet, setting `max_new_tokens`."]}
```

### Phi-1

🔗  https://huggingface.co/microsoft/phi-1

```markdown
{"error":"Input length of input_ids is 92, but `max_length` is set to 20. This can lead to unexpected behavior. You should consider increasing `max_length` or, better yet, setting `max_new_tokens`.","warnings":["There was an inference error: Input length of input_ids is 92, but `max_length` is set to 20. This can lead to unexpected behavior. You should consider increasing `max_length` or, better yet, setting `max_new_tokens`."]}
```

### Model Card for Mistral-7B-Instruct-v0.2

🔗 https://huggingface.co/mistralai/Mistral-7B-Instruct-v0.2

The model showed decent results, but during the last stages of project the server hosting Mistral-7B-Instruct-v0.2 was not operational.
