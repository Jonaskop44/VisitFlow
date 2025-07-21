"use client";

import Keycloak from "keycloak-js";
import { useEffect, useState } from "react";

const Home = () => {
  const [keycloak, setKeycloak] = useState<Keycloak | null>(null);
  const [authenticated, setAuthenticated] = useState(false);

  useEffect(() => {
    const kc = new Keycloak({
      url: "http://localhost:8080",
      realm: "visitflow",
      clientId: "visitflow-client",
    });

    kc.init({ onLoad: "login-required" }).then((authenticated) => {
      setKeycloak(kc);
      setAuthenticated(authenticated);
    });
  }, []);

  if (!keycloak) {
    return <div>Loading...</div>;
  }

  if (!authenticated) {
    return <div>Not authenticated</div>;
  }

  return (
    <div>
      <h1>Welcome to VisitFlow</h1>
      <p>
        VisitFlow is a web app for scheduling visits, managing appointments, and
        sending PDFs after payment.
      </p>
      <p>Explore the features and get started!</p>
      {keycloak.authenticated && (
        <div>
          <p>User: {keycloak.tokenParsed?.preferred_username}</p>
          <button onClick={() => keycloak.logout()}>Logout</button>
        </div>
      )}
    </div>
  );
};

export default Home;
