import { useEffect, useState } from "react";
import api from "../api/client";
import Message from "../components/Message";

export default function BeneficiariesPage() {
  const [beneficiaries, setBeneficiaries] = useState([]);
  const [form, setForm] = useState({ name: "", accountNumber: "" });
  const [message, setMessage] = useState("");
  const [type, setType] = useState("info");

  useEffect(() => {
    load();
  }, []);

  async function load() {
    try {
      const response = await api.get("/beneficiary/list");
      setBeneficiaries(response.data.data);
    } catch (error) {
      setType("error");
      setMessage(error.response?.data?.message || "Could not load beneficiaries");
    }
  }

  async function add(event) {
    event.preventDefault();
    try {
      await api.post("/beneficiary/add", form);
      setType("info");
      setMessage("Beneficiary added");
      setForm({ name: "", accountNumber: "" });
      load();
    } catch (error) {
      setType("error");
      setMessage(error.response?.data?.message || "Could not add beneficiary");
    }
  }

  async function remove(id) {
    try {
      await api.delete(`/beneficiary/${id}`);
      setType("info");
      setMessage("Beneficiary deleted");
      load();
    } catch (error) {
      setType("error");
      setMessage(error.response?.data?.message || "Could not delete beneficiary");
    }
  }

  return (
    <div className="space-y-5">
      <div>
        <h1 className="text-2xl font-semibold">Beneficiaries</h1>
        <p className="text-sm text-slate-500">Save frequent transfer recipients</p>
      </div>

      <form className="panel grid gap-3 md:grid-cols-3" onSubmit={add}>
        <div className="md:col-span-3">
          <Message message={message} type={type} />
        </div>
        <input className="field" placeholder="Name" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required />
        <input className="field" placeholder="10-digit account number" value={form.accountNumber} onChange={(e) => setForm({ ...form, accountNumber: e.target.value })} required />
        <button className="btn">Add</button>
      </form>

      <div className="panel overflow-x-auto">
        <table className="w-full border-collapse">
          <thead>
            <tr className="bg-slate-50">
              <th className="table-cell">Name</th>
              <th className="table-cell">Account Number</th>
              <th className="table-cell">Action</th>
            </tr>
          </thead>
          <tbody>
            {beneficiaries.map((beneficiary) => (
              <tr key={beneficiary.id}>
                <td className="table-cell">{beneficiary.name}</td>
                <td className="table-cell">{beneficiary.accountNumber}</td>
                <td className="table-cell">
                  <button className="btn-secondary" type="button" onClick={() => remove(beneficiary.id)}>
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
