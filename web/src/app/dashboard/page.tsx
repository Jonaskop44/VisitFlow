"use client";

import ApiClient from "@/api";
import { SummaryStatistics } from "@/types/statistics.types";
import { Badge } from "@heroui/react";
import { Icon } from "@iconify/react";
import { useEffect, useState } from "react";
import { toast } from "sonner";

const apiClient = new ApiClient();

const Home = () => {
  const [summary, setSummary] = useState<SummaryStatistics | null>(null);

  useEffect(() => {
    apiClient.statistics.helper
      .getSummary("1a62f6c9-4e66-47a7-aec5-96aed64aa4c4")
      .then((response) => {
        if (response.status) {
          setSummary(response.data);
        } else {
          toast.error("Es gab einen Fehler beim Laden der Daten");
        }
      });
  }, []);

  return (
    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 md:gap-6">
      {/* <!-- Metric Item Start --> */}
      <div className="rounded-2xl border border-gray-200 bg-white p-5 md:p-6">
        <div className="flex items-center justify-center w-12 h-12 bg-gray-100 rounded-xl">
          <Icon icon="mdi:account-group" className="text-gray-800 size-6" />
        </div>

        <div className="flex items-end justify-between mt-5">
          <div>
            <span className="text-sm text-gray-500">Kunden</span>
            <h4 className="mt-2 font-bold text-gray-800 text-title-sm">
              {summary?.totalCustomers}
            </h4>
          </div>
          <Badge color="success">
            <Icon icon="mdi:arrow-up" />
            11.01%
          </Badge>
        </div>
      </div>
      {/* <!-- Metric Item End --> */}

      {/* <!-- Metric Item Start --> */}
      <div className="rounded-2xl border border-gray-200 bg-white p-5 md:p-6">
        <div className="flex items-center justify-center w-12 h-12 bg-gray-100 rounded-xl">
          <Icon icon="mdi:package" className="text-gray-800" />
        </div>
        <div className="flex items-end justify-between mt-5">
          <div>
            <span className="text-sm text-gray-500">Bestellungen</span>
            <h4 className="mt-2 font-bold text-gray-800 text-title-sm">
              {summary?.totalOrders}
            </h4>
          </div>

          <Badge color="danger">
            <Icon icon="mdi:arrow-down" className="text-error-500" />
            9.05%
          </Badge>
        </div>
      </div>
      {/* <!-- Metric Item End --> */}
    </div>
  );
};

export default Home;
