import { useEffect, useState } from "react";
import api from "../api/client";
import Message from "../components/Message";

export default function TransferPage() {
  const [beneficiaries, setBeneficiaries] = useState([]);
  const [selectedBeneficiary, setSelectedBeneficiary] = useState("");
  const [form, setForm] = useState({ receiverAccountNumber: "", amount: "", description: "" });
  const [message, setMessage] = useState("");
  const [type, setType] = useState("info");

  useEffect(() => {
    async function loadBeneficiaries() {
      const response = await api.get("/beneficiary/list");
      setBeneficiaries(response.data.data);
    }

    loadBeneficiaries();
  }, []);

  async function submit(event) {
    event.preventDefault();
    setMessage("");

    try {
      const response = await api.post("/transfer/send", {
        receiverAccountNumber: form.receiverAccountNumber,
        amount: Number(form.amount),
        description: form.description
      });

      setType("info");
      setMessage(`Transfer successful. New balance: ₹${Number(response.data.data).toLocaleString("en-IN", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })}`);
      setForm({ receiverAccountNumber: "", amount: "", description: "" });
      setSelectedBeneficiary("");
    } catch (error) {
      setType("error");
      setMessage(error.response?.data?.message || "Transfer failed");
    }
  }

  return (
    <div className="max-w-2xl space-y-5">
      <div>
        <h1 className="text-2xl font-semibold">Transfer Money</h1>
        <p className="text-sm text-slate-500">Send money to an active account</p>
      </div>

      <form className="panel space-y-3" onSubmit={submit}>
        <Message message={message} type={type} />
        <select
          className="field"
          value={selectedBeneficiary}
          onChange={(e) => {
            setSelectedBeneficiary(e.target.value);
            setForm((current) => ({ ...current, receiverAccountNumber: e.target.value }));
          }}
        >
          <option value="">Select a saved beneficiary</option>
          {beneficiaries.map((beneficiary) => (
            <option key={beneficiary.id} value={beneficiary.accountNumber}>
              {beneficiary.name} — {beneficiary.accountNumber}
            </option>
          ))}
        </select>
        <input
          className="field"
          placeholder="Receiver account number"
          value={form.receiverAccountNumber}
          onChange={(e) => {
            setSelectedBeneficiary("");
            setForm((current) => ({ ...current, receiverAccountNumber: e.target.value }));
          }}
          required
        />
        <input
          className="field"
          type="number"
          min="1"
          step="0.01"
          placeholder="Amount"
          value={form.amount}
          onChange={(e) => setForm({ ...form, amount: e.target.value })}
          required
        />
        <input
          className="field"
          placeholder="Description"
          value={form.description}
          onChange={(e) => setForm({ ...form, description: e.target.value })}
        />
        <button className="btn">Send Money</button>
      </form>
    </div>
  );
}
