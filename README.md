* install and start keycloak: https://keycloak.gitbooks.io/documentation/getting_started/topics/first-boot.html
* create admin user
* create client with id `api-admin`
* enable authorization for `api-admin` (valid redirect URI can be http://localhost:9000)
* copy clientSecret to KeycloakService. ClientSecret is visible in Credentials tab.
* create user `api-admin` with password `pass`
