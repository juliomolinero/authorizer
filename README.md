## Authorizer Code Challenge

You are tasked with implementing an application that authorizes a transaction for a specific account following a set of
predefined rules.

Your program is going to be provided json lines as input in the stdin , and should provide a json line output for each
one — imagine this as a stream of events arriving at the authorizer.

#### State
The program should not rely on any external database. Internal state should be handled by an explicit in-memory structure.
State is to be reset at application start.

#### Operations
The program handles two kinds of operations, deciding on which one according to the line that is being processed:
1. Account creation
2. Transaction authorization
   
For the sake of simplicity, you can assume all monetary values are integers, using a currency without cents.

#### Steps
1. Console application receives a file with several json lines
2. Operation service receives a single json line, makes use of Utils class to parse it as a JSONObject which will be parsed into either an account or transaction
3. Operation service receives an object [account | transaction] and apply its respective business rules (card is active, available limit, etc.)
4. Operation service stores data into the account services and transaction service
5. Operation service sends a response (string) to console application
6. Console application writes to output file (nubank-auth-<TIMESTAMP>) every single response line

### Installation - Assuming Docker is installed [Get Docker](https://docs.docker.com/get-docker/) 
#### Build Docker Image
```
$ docker build --pull \
  -t nubank-auth-svc:latest \
  -f Dockerfile .
```

### Usage - Assuming Docker is installed [Get Docker](https://docs.docker.com/get-docker/)
#### Run Docker image
```
# This will execute and remove the container when is done
# IMPORTANT!, no matter if you want to use another volume "utils" folder MUST exist
# or the application won't be able to write results

$ docker run -i --rm \
  --volume $(pwd)/utils/:/utils/ \
  nubank-auth-svc:latest /utils/input_operations

2021-02-19 19:18:02 INFO  MainApp:36 - File to process: /utils/input_operations
2021-02-19 19:18:02 INFO  MainApp:49 - Reading from file /utils/input_operations
2021-02-19 19:18:03 INFO  MainApp:57 - 9 lines processed, check out on file nubank-auth-1613762282909 for details
```



```
$ cat nubank-auth-1613762282909
{ account: { "activeCard": true, "availableLimit": 100 }, "violations": [] }
{ account: { "activeCard": true, "availableLimit": 80 }, "violations": [] }
{ account: { "activeCard": true, "availableLimit": 60 }, "violations": [] }
{ account: { "activeCard": true, "availableLimit": 40 }, "violations": [] }
{ account: { "activeCard": true, "availableLimit": 40 }, "violations": [ "insufficient-limit" ] }
{ account: { "activeCard": true, "availableLimit": 40 }, "violations": [ "insufficient-limit" , "doubled-transaction" ] }
{ account: { "activeCard": true, "availableLimit": 40 }, "violations": [ "insufficient-limit" , "doubled-transaction" ] }
{ account: { "activeCard": true, "availableLimit": 40 }, "violations": [ "insufficient-limit" , "doubled-transaction" ] }
{ account: { "activeCard": true, "availableLimit": 40 }, "violations": [ "account-already-initialized" ] }
```

### Usage - Assuming Java 11 is installed locally
```
$ cat input_operations 
{ "account": { "activeCard": true, "availableLimit": 100 } }
{ "transaction": { "merchant": "Burger King", "amount": 20, "time": "2019-02-13T10:00:00.000Z" } }
{ "transaction": { "merchant": "Burger King", "amount": 20, "time": "2019-02-13T10:03:00.000Z" } }
{ "transaction": { "merchant": "Burger King", "amount": 20, "time": "2019-02-13T10:06:00.000Z" } }
{ "transaction": { "merchant": "Habbib's", "amount": 90, "time": "2019-02-13T11:00:00.000Z" } }
{ "transaction": { "merchant": "Habbib's", "amount": 90, "time": "2019-02-13T11:01:00.000Z" } }
{ "transaction": { "merchant": "Habbib's", "amount": 90, "time": "2019-02-13T11:02:00.000Z" } }
{ "transaction": { "merchant": "Habbib's", "amount": 90, "time": "2019-02-13T11:03:00.000Z" } }
```

```
$ java -jar nubank-authorizer-1.0.jar input_operations
br.nubank.MainApp input_operations
2021-02-19 08:53:21 INFO  MainApp:48 - Reading from file input_operations
2021-02-19 08:53:22 INFO  MainApp:56 - 9 lines processed, check out on file nubank-auth-1613746401938 for details

Process finished with exit code 0

```
```
$ cat nubank-auth-1613746401938
{ account: { "activeCard": true, "availableLimit": 100 }, "violations": [] }
{ account: { "activeCard": true, "availableLimit": 80 }, "violations": [] }
{ account: { "activeCard": true, "availableLimit": 60 }, "violations": [] }
{ account: { "activeCard": true, "availableLimit": 40 }, "violations": [] }
{ account: { "activeCard": true, "availableLimit": 40 }, "violations": [ "insufficient-limit" ] }
{ account: { "activeCard": true, "availableLimit": 40 }, "violations": [ "insufficient-limit" , "doubled-transaction" ] }
{ account: { "activeCard": true, "availableLimit": 40 }, "violations": [ "insufficient-limit" , "doubled-transaction" ] }
{ account: { "activeCard": true, "availableLimit": 40 }, "violations": [ "insufficient-limit" , "doubled-transaction" ] }
```
