import { Card, CardBody } from "@heroui/react";
import { motion } from "framer-motion";
import { FC } from "react";
import { Icon } from "@iconify/react";

interface CompanyNotFoundProps {
  companyId: string;
}

const CompanyNotFound: FC<CompanyNotFoundProps> = ({ companyId }) => {
  return (
    <div className="min-h-screen flex items-center justify-center p-4">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
      >
        <Card className="shadow-2xl border-1 border-gray-300">
          <CardBody className="text-center p-8">
            <motion.div
              initial={{ scale: 0 }}
              animate={{ scale: 1 }}
              transition={{ delay: 0.2, duration: 0.5, type: "spring" }}
            >
              <Icon
                icon="line-md:alert-twotone"
                className="w-20 h-20 text-orange-500 mx-auto mb-6"
              />
            </motion.div>

            <motion.div
              initial={{ opacity: 0, y: 10 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.4, duration: 0.5 }}
            >
              <h1 className="text-3xl font-bold text-gray-800 mb-4">
                Unternehmen nicht gefunden
              </h1>
              <p className="text-gray-600 mb-2">
                Das Unternehmen mit der ID{" "}
                <span className="font-mono bg-gray-100 px-2 py-1 rounded text-sm">
                  {companyId}
                </span>{" "}
                konnte nicht gefunden werden.
              </p>
              <p className="text-gray-500 text-sm mb-8">
                Möglicherweise ist der Link ungültig oder das Unternehmen ist
                nicht mehr verfügbar.
              </p>
            </motion.div>
          </CardBody>
        </Card>
      </motion.div>
    </div>
  );
};

export default CompanyNotFound;
