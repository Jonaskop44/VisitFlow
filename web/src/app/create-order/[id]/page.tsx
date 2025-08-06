"use client";

import ApiClient from "@/api";
import AddressSection from "@/components/Order/AddressSection";
import CompanyNotFound from "@/components/Order/CompanyNotFound";
import ContactInfoSection from "@/components/Order/ContactInfoSection";
import OrderDetailsSection from "@/components/Order/OrderDetailsSection";
import PersonalInfoSection from "@/components/Order/PersonalInfoSection";
import { OrderFormData, orderSchema } from "@/lib/order-validation";
import { Company } from "@/types/company.types";
import { Button, Card, CardBody, CardHeader } from "@heroui/react";
import { zodResolver } from "@hookform/resolvers/zod";
import { motion } from "framer-motion";
import { useParams } from "next/navigation";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { toast } from "sonner";

const apiClient = new ApiClient();

const CreateOrderPage = () => {
  const params = useParams();
  const companyId = params.id as string;
  const [company, setCompany] = useState<Company | null>(null);
  const [isSubmitted, setIsSubmitted] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const {
    register,
    control,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<OrderFormData>({
    resolver: zodResolver(orderSchema),
  });

  useEffect(() => {
    apiClient.company.helper.getCompanyInfo(companyId).then((response) => {
      if (response.status) {
        setCompany(response.data);
      }
    });
  }, [companyId]);

  if (!company) {
    return <CompanyNotFound companyId={companyId} />;
  }

  const onSubmit = async (data: OrderFormData) => {
    setIsSubmitting(true);

    apiClient.order.helper.createOrder(data, companyId).then((response) => {
      if (response.status) {
        setIsSubmitted(true);
        setIsSubmitting(false);
        reset();
      } else {
        setIsSubmitting(false);
        toast.error(
          "Fehler beim Senden der Buchung. Bitte versuchen Sie es später erneut."
        );
      }
    });
  };

  return (
    <div className="min-h-screen bg-neutral-page py-8 px-4">
      <div className="max-w-4xl mx-auto">
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
        >
          <Card className="w-full">
            <CardHeader className="pb-6">
              <div className="text-center w-full">
                <h1 className="text-3xl font-bold text-gray-800 mb-2">
                  Service Buchung
                </h1>
                <p className="text-gray-600">{company.description}</p>
              </div>
            </CardHeader>
            <CardBody>
              <form onSubmit={handleSubmit(onSubmit)} className="space-y-8">
                <PersonalInfoSection register={register} errors={errors} />
                <ContactInfoSection register={register} errors={errors} />
                <AddressSection register={register} errors={errors} />
                <OrderDetailsSection
                  register={register}
                  control={control}
                  errors={errors}
                  products={company.products}
                />

                <motion.div
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  transition={{ duration: 0.5, delay: 0.4 }}
                  className="pt-6"
                >
                  <Button
                    type="submit"
                    color="primary"
                    size="lg"
                    className="w-full"
                    isLoading={isSubmitting}
                    disabled={isSubmitting}
                  >
                    {isSubmitting
                      ? "Wird gesendet..."
                      : `Buchung an ${company.name} senden`}
                  </Button>
                </motion.div>
              </form>
            </CardBody>
          </Card>
        </motion.div>
      </div>
    </div>
  );
};

export default CreateOrderPage;
