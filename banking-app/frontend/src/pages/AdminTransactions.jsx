import { useEffect, useState } from "react";
import api from "../api/client";

function formatAmount(value) {
  const number = Number(value ?? 0);
  return number.toLocaleString("en-IN", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

export default function AdminTransactions() {
  const [transactions, setTransactions] = useState([]);

  useEffect(() => {
    async function load() {
      const response = await api.get("/admin/transactions");
      setTransactions(response.data.data);
    }

    load();
  }, []);

  return (
    <div className="space-y-5">
      <div>
        <h1 className="text-2xl font-semibold">Admin Transactions</h1>
        <p className="text-sm text-slate-500">All transactions across the bank</p>
      </div>

      <div className="panel overflow-x-auto">
        <table className="w-full border-collapse">
          <thead>
            <tr className="bg-slate-50">
              <th className="table-cell">Date</th>
              <th className="table-cell">Sender</th>
              <th className="table-cell">Receiver</th>
              <th className="table-cell">Type</th>
              <th className="table-cell">Amount</th>
              <th className="table-cell">Description</th>
            </tr>
          </thead>
          <tbody>
            {transactions.map((transaction) => (
              <tr key={transaction.id}>
                <td className="table-cell">{new Date(transaction.createdAt).toLocaleString()}</td>
                <td className="table-cell">{transaction.senderAccount}</td>
                <td className="table-cell">{transaction.receiverAccount}</td>
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
