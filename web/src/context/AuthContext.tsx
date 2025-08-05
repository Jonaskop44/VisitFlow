import keycloak from "@/lib/keycloak";
import { createContext, FC, useEffect, useState } from "react";

export interface AuthContextType {
  token: string | null;
  initialized: boolean;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextType>({
  token: null,
  initialized: false,
  logout: () => {},
});

interface AuthProviderProps {
  children: React.ReactNode;
}

const AuthProvider: FC<AuthProviderProps> = ({ children }) => {
  const [token, setToken] = useState<string | null>(null);
  const [initialized, setInitialized] = useState<boolean>(false);

  useEffect(() => {
    keycloak
      .init({
        onLoad: "login-required",
        checkLoginIframe: false,
        pkceMethod: "S256",
        flow: "standard",
      })
      .then((authenticated) => {
        if (authenticated) {
          setToken(keycloak.token || null);
        }
        setInitialized(true);
      });
  }, []);

  const logout = () => {
    keycloak.logout();
    setToken(null);
  };

  return (
    <AuthContext.Provider value={{ token, initialized, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export default AuthProvider;
