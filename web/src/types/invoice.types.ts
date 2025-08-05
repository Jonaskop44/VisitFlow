enum InvoiceStatus {
  PENDING,
  PAID,
  FAILED,
  CANCELED,
}

export interface Invoice {
  id: number;
  stripePaymentId: string | null;
  stripeSessionId: string | null;
  status: InvoiceStatus;
  paidAt: Date | null;
  token: string;
  createdAt: Date;
  updatedAt: Date;
}
