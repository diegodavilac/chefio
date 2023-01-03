package dev.diegodc.chefio.data.sources.receipt.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import dev.diegodc.chefio.data.sources.receipt.ReceiptDataSource
import dev.diegodc.chefio.models.Receipt
import dev.diegodc.chefio.models.UserProfile
import kotlinx.coroutines.tasks.await
import java.util.*

class ReceiptRemoteDataSource(
    val firestore: FirebaseFirestore
) : ReceiptDataSource {

    var lastReceiptVisible: DocumentSnapshot? = null

    companion object {
        const val PAGE_SIZE = 20L
    }

    override suspend fun createOrUpdateReceipt(receipt: Receipt): String {
        val result = firestore.collection("receipts")
            .add(receipt)
            .await()

        return result.id
    }

    override suspend fun loadReceipts(clean: Boolean): List<Receipt> {
        if (clean) {
            lastReceiptVisible = null
        }

        val request = firestore.collection("receipts")
            .orderBy("createdAt")
            .limit(Companion.PAGE_SIZE)

        if (lastReceiptVisible != null) {
            request.startAfter(lastReceiptVisible)
        }

        val result = request.get().await()

        lastReceiptVisible = result.documents.last()
        return result.documents.map { snap ->
            val data = snap.data
            Receipt(
                id = snap.id,
                creator = UserProfile(
                    name = "",
                    lastName = "",
                    username = "",
                    photo = ""
                ),
                image = "",
                title = (data?.get("title") as String?) ?: "",
                createdAt = (data?.get("createdAt") as Timestamp).toDate()
            )
        }
    }

}