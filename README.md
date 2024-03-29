**Todo App with:**<br>
+ User Authentication + Spring boot + Spring Data Jpa(Hibernate) + Oauth2 Resource Server (JSON Web Token JWT)
+ Postgres Database + Unit Testing using JUnit, AssertJ and Mockito
+ Angular, PrimeNg for components, PrimeFlex for styling and PrimeIcons for icons + Postman and javascript for API testing
+ Selenium(Python) and Chrome webdriver for UI testing + AWS EC2, VPC, S3, NGINX, SSH for deployment + Docker, docker-compose and Github Actions
for CI/CD pipeline.

**Open Live**: http://ec2-3-75-210-228.eu-central-1.compute.amazonaws.com <br>

**How the application works?**<br>
This is a classic todo app with user authentication and data persistence on Postgres. The user login then he can add his todo, set the status the
todo which represented by a percentage of complettion.<br>
The user can also delete some todos or update them.

**If you don't want to create an account**<br>
You can use these this user to test the functionalities of the application:<br>
email: fede.valverde@fmail.com && password: Password1234#


**The Backend**<br>
For the backend I used Spring Boot 3, Spring Data Jpa (Hibernate) and Postgres Database for data persistence. I have implemented
the authentication flow from regitering the user to login, reset the password and logout. The Oauth2 Resource server uses public 
and private key pair generated by openssl rsa to generate via the JwtEncoder and decode via JwtDecoder our JSON Web Tokens and thus 
authenticate requests. The JWT is sent to the client when he is authenticated to allow him to make the subsequent requests: create,
update or delete todos.

**The Frontend**<br>
For the frontend I used angular, PrimeNg for the different components such as Dialog, Toast, form inputs. In addition I used PrimeFlex
for styling and PrimeIcon for icons.

**Testing**<br>
I wrote unit tests for my backend using JUnit, AssertJ and Mockito.<br>
I wrote tests for my RestAPI on postman<br>
I automated my UI testing using Selenium and Chrome driver

**DevOps**<br>
I used docker to containerize my application for easier deployment on AWS.<br>
I created three Dockerfiles: one for the frontend, one for the backend and one for my database.<br>
I created a CI/CD pipeline using github actions that will run my docker-compose file to build the 3 images corresponding to 
my frontend, my beckend and my database. Then it will push those images to docker hub.<br>
Then I load the docker-compose file that will allow me to pull my images and run my containers to S3 bucket and I make it public <br>
so it can be downloaded.<br>
Then I SSH to my EC2 to download my docker-compose file and pull my images and run my comtainers.
