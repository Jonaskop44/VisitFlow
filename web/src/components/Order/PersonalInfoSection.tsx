import { motion } from "framer-motion";
import { UseFormRegister, FieldErrors } from "react-hook-form";
import { FC } from "react";
import { Input } from "@heroui/react";
import { OrderFormData } from "@/lib/order-validation";

interface PersonalInfoSectionProps {
  register: UseFormRegister<OrderFormData>;
  errors: FieldErrors<OrderFormData>;
}

const PersonalInfoSection: FC<PersonalInfoSectionProps> = ({
  register,
  errors,
}) => {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5 }}
      className="space-y-4"
    >
      <h3 className="text-lg font-semibold text-gray-800">Persönliche Daten</h3>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <Input
          {...register("firstName")}
          label="Vorname"
          variant="bordered"
          isRequired
          isInvalid={!!errors.firstName}
          errorMessage={errors.firstName?.message}
        />
        <Input
          {...register("lastName")}
          label="Nachname"
          variant="bordered"
          isRequired
          isInvalid={!!errors.lastName}
          errorMessage={errors.lastName?.message}
        />
      </div>
    </motion.div>
  );
};

export default PersonalInfoSection;
