import axios from "axios";

export class Helper {
  constructor() {}

  async uploadFile(
    invoiceId: number,
    files: File[],
    onProgress: (progress: number) => void
  ) {
    const formData = new FormData();
    files.forEach((file) => formData.append("file", file));

    return axios
      .post(`pdf/${invoiceId}/upload`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
        onUploadProgress: (progressEvent) => {
          const percentage = Math.round(
            progressEvent.total
              ? (progressEvent.loaded * 100) / progressEvent.total
              : 0
          );
          onProgress(percentage);
        },
      })
      .then((response) => {
        if (response.status !== 200) return { data: null, status: false };

        return { data: null, status: true };
      })
      .catch(() => {
        return { data: null, status: false };
      });
  }

  async downloadFile(
    invoiceToken: number,
    onProgress: (progress: number) => void
  ) {
    return axios
      .get(`pdf/${invoiceToken}/download`, {
        responseType: "blob",
        onDownloadProgress: (progressEvent) => {
          if (progressEvent.total) {
            const progress = (progressEvent.loaded / progressEvent.total) * 100;
            onProgress(progress);
          }
        },
      })
      .then((response) => {
        if (response.status !== 200) return { status: false };

        const blob = new Blob([response.data]);
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", `invoice-${invoiceToken}.pdf`);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);

        return { status: true };
      })
      .catch(() => {
        return { status: false };
      });
  }
}
