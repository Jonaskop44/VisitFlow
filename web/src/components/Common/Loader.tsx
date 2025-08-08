"use client";

import { Spinner } from "@heroui/react";
import { FC } from "react";

interface LoaderProps {
  label?: string;
}

const Loader: FC<LoaderProps> = ({ label }) => {
  return (
    <div className="flex h-screen items-center justify-center bg-[#f3f4f6]">
      <Spinner
        variant="gradient"
        size="lg"
        label={label ? label : "Loading..."}
      />
    </div>
  );
};

export default Loader;
