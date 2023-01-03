package dev.diegodc.chefio.data.repositories

import dev.diegodc.chefio.models.Receipt
import dev.diegodc.chefio.data.Result

interface IReceiptsRepository {
    suspend fun saveReceipt(receipt: Receipt): Result<String>
    suspend fun getReceipts(page:Int) : Result<List<Receipt>>
    suspend fun deleteReceipt(receiptId: String)
}