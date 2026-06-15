import { useEffect, useState } from "react";
import api from "../api/client";
import Message from "../components/Message";

function statusClass(status) {
  return status === "BLOCKED"
    ? "bg-red-100 text-red-700"
    : "bg-green-100 text-green-700";
}

export default function ProfilePage() {
  const [profile, setProfile] = useState(null);
  const [profileForm, setProfileForm] = useState({ email: "", phone: "" });
  const [passwordForm, setPasswordForm] = useState({ currentPassword: "", newPassword: "" });
  const [message, setMessage] = useState("");
  const [type, setType] = useState("info");

  useEffect(() => {
    load();
  }, []);

  async function load() {
    try {
      const response = await api.get("/profile/");
      setProfile(response.data.data);
      setProfileForm({ email: response.data.data.email, phone: response.data.data.phone });
    } catch (error) {
      setType("error");
      setMessage(error.response?.data?.message || "Profile could not be loaded");
    }
  }

  async function updateProfile(event) {
    event.preventDefault();
    try {
      const response = await api.put("/profile/update", profileForm);
      setProfile(response.data.data);
      setType("info");
      setMessage("Profile updated. Please login again if you changed your email.");
    } catch (error) {
      setType("error");
      setMessage(error.response?.data?.message || "Profile update failed");
    }
  }

  async function changePassword(event) {
    event.preventDefault();
    try {
      await api.put("/profile/change-password", passwordForm);
      setType("info");
      setMessage("Password changed");
      setPasswordForm({ currentPassword: "", newPassword: "" });
    } catch (error) {
      setType("error");
      setMessage(error.response?.data?.message || "Password change failed");
    }
  }

  return (
    <div className="max-w-3xl space-y-5">
      <div>
        <h1 className="text-2xl font-semibold">Profile</h1>
        <p className="text-sm text-slate-500">Manage your contact details and password</p>
      </div>

      <Message message={message} type={type} />

      <div className="panel space-y-2">
        <p className="text-lg font-semibold">{profile?.fullName || "-"}</p>
        <p className="text-sm text-slate-600">{profile?.email || "-"}</p>
        <p className="text-sm text-slate-600">{profile?.phone || "-"}</p>
        <p className="text-sm text-slate-600">Account: {profile?.accountNumber || "-"}</p>
        <span className={`inline-flex rounded-full px-3 py-1 text-sm font-medium ${statusClass(profile?.accountStatus)}`}>
          {profile?.accountStatus || "-"}
        </span>
      </div>

      <form className="panel space-y-3" onSubmit={updateProfile}>
        <h2 className="font-semibold">Update Contact</h2>
        <input className="field" type="email" value={profileForm.email} onChange={(e) => setProfileForm({ ...profileForm, email: e.target.value })} required />
        <input className="field" value={profileForm.phone} onChange={(e) => setProfileForm({ ...profileForm, phone: e.target.value })} required />
        <button className="btn">Save</button>
      </form>

      <form className="panel space-y-3" onSubmit={changePassword}>
        <h2 className="font-semibold">Change Password</h2>
        <input className="field" type="password" placeholder="Current password" value={passwordForm.currentPassword} onChange={(e) => setPasswordForm({ ...passwordForm, currentPassword: e.target.value })} required />
        <input className="field" type="password" minLength="8" placeholder="New password" value={passwordForm.newPassword} onChange={(e) => setPasswordForm({ ...passwordForm, newPassword: e.target.value })} required />
        <button className="btn">Change Password</button>
      </form>
    </div>
  );
}
