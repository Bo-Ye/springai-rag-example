# Spring AI with Azure OpenAI Chat completion

A sample project is intended as a tutorial to demonstrate the ease of developing an AI application using SpringAI and Azure OpenAI. It intendes to follow a sequence that starts with a simplest of the use case and adds an advanced use case with each increment. The details of each step is given under the "Concepts covered" section below. 

Each concept on this topic is separated into a different package. The corresponding README files can be found at the root of this project. 

## Local Setup
The RAG module of this project uses a local docker instance of ChromaDB at port 8000. In order to start this application in local, the ChromaDB needs to be running and the local port 8000 need to forward the traffic to the port 8000 in Docker. 

If using the Docker Desktop UI, this can be done by providing `Host Port` as 8000 under the `Optional Settings` while starting a new container from the image. If using the CLI, the property `-p 8000:8000` should be provided to the `docker run` command

## Concepts covered
1. Simple Chat: 1-README-chat-completion.md
2. Chat enhanced with RAG: 2-README-retrieval-augmented-generation.md 