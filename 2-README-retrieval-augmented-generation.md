# Spring AI Retrieval Augmented Generation (RAG) with Azure OpenAI

A sample project that demonstrates the ease of developing a RAG AI application using SpringAI and Azure OpenAI

## Installation

* Create the Azure OpenAI Service and a Deployment. The Azure OpenAI Service can be created using the Azure Portal and the Deployment can be created in the Azure AI Foundry
* Create a deployment for text-embedding-ada-002 model in the Azure AI Foundry. If a different embedding model is used, the corresponding values need to be provided in the `application.properties` file
* Note down the API Key, the API endpoint for the Azure OpenAI Service and the name of the LLM Deployment and the model used from the Azure AI Foundry
* Replace these values in the `application.properties` file

## Additional Details
A detailed description about this implementation can be found [here](https://smoothed9.medium.com/retrieval-augmented-generation-rag-with-spring-ai-a68bc0c57fcc)
