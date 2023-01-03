package dev.diegodc.chefio.data.sources.receipt

import dev.diegodc.chefio.models.Receipt

interface ReceiptDataSource {
    suspend fun createOrUpdateReceipt(receipt: Receipt ) : String
    suspend fun loadReceipts(clean:Boolean) : List<Receipt>
}