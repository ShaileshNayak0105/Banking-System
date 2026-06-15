import { useEffect, useState } from "react";
import api from "../api/client";
import Message from "../components/Message";

function formatAmount(value) {
  const number = Number(value ?? 0);
  return number.toLocaleString("en-IN", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

export default function TransactionsPage() {
  const [transactions, setTransactions] = useState([]);
  const [filters, setFilters] = useState({ startDate: "", endDate: "", type: "", minAmount: "", maxAmount: "" });
  const [message, setMessage] = useState("");
  const [type, setType] = useState("info");

  useEffect(() => {
    loadHistory();
  }, []);

  async function loadHistory() {
    try {
      const response = await api.get("/transactions/history");
      setTransactions(response.data.data);
    } catch (error) {
      setType("error");
      setMessage(error.response?.data?.message || "Failed to load transactions");
    }
  }

  async function search(event) {
    event.preventDefault();

    try {
      const params = Object.fromEntries(Object.entries(filters).filter(([, value]) => value !== ""));
      const response = await api.get("/transactions/search", { params });
      setTransactions(response.data.data);
      setType("info");
      setMessage("Transactions filtered");
    } catch (error) {
      setType("error");
      setMessage(error.response?.data?.message || "Search failed");
    }
  }

  async function download(path, filename) {
    try {
      const response = await api.get(path, { responseType: "blob" });
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.download = filename;
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      setType("error");
      setMessage(error.response?.data?.message || "Download failed");
    }
  }

  return (
    <div className="space-y-5">
      <div>
        <h1 className="text-2xl font-semibold">Transactions</h1>
        <p className="text-sm text-slate-500">Search your history and download statements</p>
      </div>

      <Message message={message} type={type} />

      <form className="panel grid gap-3 md:grid-cols-6" onSubmit={search}>
        <input className="field" type="date" value={filters.startDate} onChange={(e) => setFilters({ ...filters, startDate: e.target.value })} />
        <input className="field" type="date" value={filters.endDate} onChange={(e) => setFilters({ ...filters, endDate: e.target.value })} />
        <select className="field" value={filters.type} onChange={(e) => setFilters({ ...filters, type: e.target.value })}>
          <option value="">All types</option>
          <option value="CREDIT">Credit</option>
          <option value="DEBIT">Debit</option>
        </select>
        <input className="field" type="number" placeholder="Min amount" value={filters.minAmount} onChange={(e) => setFilters({ ...filters, minAmount: e.target.value })} />
        <input className="field" type="number" placeholder="Max amount" value={filters.maxAmount} onChange={(e) => setFilters({ ...filters, maxAmount: e.target.value })} />
        <button className="btn">Search</button>
      </form>

      <div className="flex flex-wrap gap-2">
        <button className="btn-secondary" type="button" onClick={() => download("/transactions/export/pdf", "statement.pdf")}>
          Download PDF
        </button>
        <button className="btn-secondary" type="button" onClick={() => download("/transactions/export/csv", "statement.csv")}>
          Download CSV
        </button>
      </div>

      <div className="panel overflow-x-auto">
        <table className="w-full border-collapse">
          <thead>
            <tr className="bg-slate-50">
              <th className="table-cell">Date</th>
              <th className="table-cell">Type</th>
              <th className="table-cell">Amount</th>
              <th className="table-cell">Description</th>
            </tr>
          </thead>
          <tbody>
            {transactions.map((transaction) => (
              <tr key={transaction.id}>
                <td className="table-cell">{new Date(transaction.createdAt).toLocaleString()}</td>
                <td className="table-cell">{transaction.type}</td>
                <td className="table-cell">₹{formatAmount(transaction.amount)}</td>
                <td className="table-cell">{transaction.description}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
