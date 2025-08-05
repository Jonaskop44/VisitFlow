"use client";

import Loader from "@/components/Common/Loader";
import { useUserStore } from "@/data/user-store";
import { FC, useEffect } from "react";

interface AuthProviderProps {
  children: React.ReactNode;
}

const AuthProvider: FC<AuthProviderProps> = ({ children }) => {
  const { init, initialized } = useUserStore();

  useEffect(() => {
    init();
  }, [init]);

  if (!initialized) {
    return <Loader />;
  }

  return <>{children}</>;
};

export default AuthProvider;
