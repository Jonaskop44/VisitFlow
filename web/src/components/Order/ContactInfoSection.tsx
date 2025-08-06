import { OrderFormData } from "@/lib/order-validation";
import { Input } from "@heroui/react";
import { motion } from "framer-motion";
import { FC } from "react";
import { UseFormRegister, FieldErrors } from "react-hook-form";

interface ContactInfoSectionProps {
  register: UseFormRegister<OrderFormData>;
  errors: FieldErrors<OrderFormData>;
}

const ContactInfoSection: FC<ContactInfoSectionProps> = ({
  register,
  errors,
}) => {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5, delay: 0.1 }}
      className="space-y-4"
    >
      <h3 className="text-lg font-semibold text-gray-800">Kontaktdaten</h3>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <Input
          {...register("email")}
          label="E-Mail"
          type="email"
          variant="bordered"
          isRequired
          isInvalid={!!errors.email}
          errorMessage={errors.email?.message}
        />
        <Input
          {...register("phoneNumber")}
          label="Telefonnummer"
          isRequired
          type="tel"
          variant="bordered"
          isInvalid={!!errors.phoneNumber}
          errorMessage={errors.phoneNumber?.message}
        />
      </div>
    </motion.div>
  );
};

export default ContactInfoSection;
