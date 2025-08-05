import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "http://localhost:8080",
  realm: "visitflow",
  clientId: "visitflow-client",
});

export default keycloak;
