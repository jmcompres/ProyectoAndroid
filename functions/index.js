const { onDocumentCreated } = require("firebase-functions/v2/firestore");
const { initializeApp } = require("firebase-admin/app");
const { getFirestore, FieldValue } = require("firebase-admin/firestore");
const { getMessaging } = require("firebase-admin/messaging");

initializeApp();

exports.notificarMensaje = onDocumentCreated(
  {
    document: "Chats/{chatId}/Messages/{messageId}",
    region: "us-central1",
  },
  async (event) => {
    const msg = event.data?.data();
    if (!msg) return;

    const db = getFirestore();

    const chatDoc = await db.collection("Chats").doc(event.params.chatId).get();
    const participantIds = chatDoc.get("participantIds") || [];

    const receiverId = participantIds.find((id) => id !== msg.senderId);
    if (!receiverId) return;

    const receiverDoc = await db.collection("users").doc(receiverId).get();
    const token = receiverDoc.get("fcmToken");
    if (!token) return;

    const senderName = msg.senderName || "Nuevo mensaje";

    try {
      await getMessaging().send({
        token,
        data: {
          title: senderName,
          body: msg.text || "",
          senderId: msg.senderId,
          senderName: senderName,
        },
        android: { priority: "high" },
      });
    } catch (error) {
      if (error.code === "messaging/registration-token-not-registered") {
        await receiverDoc.ref.update({ fcmToken: FieldValue.delete() });
      } else {
        console.error("Error enviando notificación:", error);
      }
    }
  }
);