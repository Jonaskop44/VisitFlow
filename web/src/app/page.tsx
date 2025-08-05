"use client";

import { useUserStore } from "@/data/user-store";

const Home = () => {
  const { username, logout } = useUserStore();

  return (
    <div>
      <h1>Welcome to VisitFlow</h1>
      <p>
        VisitFlow is a web app for scheduling visits, managing appointments, and
        sending PDFs after payment.
      </p>
      <p>Explore the features and get started!</p>
      {username && <p>Hello, {username}!</p>}
      {!username && <p>Please log in to access your account.</p>}
      <button
        onClick={() => {
          logout();
        }}
      >
        Log Out
      </button>
    </div>
  );
};

export default Home;
