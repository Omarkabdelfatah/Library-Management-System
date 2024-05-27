There are the steps to run this application:
1- Connect to your database spring.datasource.url=Database URL.
2- Run the application and check tha tables in your database.
3- To access any endpoint in the project you must be logged in.
4- You must register first by access this endpoint localhost:8080/auth/register with POST and json body like this 
{
    "username":"your name",
    "password":"your password"
}
5- Now you must be log in by access this endpoind localhost:8080/auth/login with POST and json body like this 
{
    "username":"your name",
    "password":"your password"
} 
and copy the returned token in variable.
6- To complete the functionalities of the project when you access any endpoint in the authorization select (Bearer Token) and put the returned token in the form.
7- Now you have to test the functionalities of the project.
