#!/bin/bash

PYTHON_FILE_PATH=$1 # default "./src/data/Many.py"
FILE_CONTENT=$(<"$PYTHON_FILE_PATH" jq -Rs .)
CURL_PAYLOAD=$(jq -n --arg content "$FILE_CONTENT" \
    '{inputs: ("Fix all the errors in the python code, give only correct code as output" + $content + "write only corrected code without any words\n\n")}')

curl https://api-inference.huggingface.co/models/mistralai/Mixtral-8x7B-Instruct-v0.1 \
    -X POST \
    -d "$CURL_PAYLOAD" \
    -H 'Content-Type: application/json' \
    -H "Authorization: " > answer.json # replace with your personal token

if [ -f "answer.json" ]; then
    GENERATED_TEXT=$(jq -r '.[0].generated_text' answer.json)
    GENERATED_TEXT=$(sed 's/return only code without any words\\n\\n//g' <<< "$GENERATED_TEXT")
    jq --arg modified_text "$GENERATED_TEXT" '.[0].generated_text = $modified_text' answer.json > temp.json && mv temp.json answer.json
else
    echo "Error: answer.json file not found."
fi
