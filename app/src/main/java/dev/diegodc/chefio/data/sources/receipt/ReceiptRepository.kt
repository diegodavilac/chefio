package dev.diegodc.chefio.data.sources.receipt

import dev.diegodc.chefio.data.repositories.IReceiptsRepository
import dev.diegodc.chefio.models.Receipt
import dev.diegodc.chefio.data.Result
import dev.diegodc.chefio.models.UserProfile
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class ReceiptRepository(
    val remoteDataSource: ReceiptDataSource
) : IReceiptsRepository {
    override suspend fun saveReceipt(receipt: Receipt): Result<String> {
        return try {
            Result.Success(remoteDataSource.createOrUpdateReceipt(receipt = receipt))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getReceipts(page: Int): Result<List<Receipt>> {
        delay(3000L)
        return Result.Success(listOf(
            Receipt(
                id = "test",
                title = "Test receipt",
                timeToPrepare = 60,
                image = "",
                isFavorite = true,
                creator = UserProfile(
                    name = "Lorem",
                    lastName = "Ipsum",
                    username = "lorem_ipsum",
                    photo = ""
                ),
                createdAt = Date()
            ),
            Receipt(
                id = "test",
                title = "Test receipt",
                timeToPrepare = 60,
                image = "",
                isFavorite = true,
                creator = UserProfile(
                    name = "Lorem",
                    lastName = "Ipsum",
                    username = "lorem_ipsum",
                    photo = ""
                ),
                createdAt = Date()
            ),
            Receipt(
                id = "test",
                title = "Test receipt",
                timeToPrepare = 60,
                image = "",
                isFavorite = true,
                creator = UserProfile(
                    name = "Lorem",
                    lastName = "Ipsum",
                    username = "lorem_ipsum",
                    photo = ""
                ),
                createdAt = Date()
            ),
            Receipt(
                id = "test",
                title = "Test receipt",
                timeToPrepare = 60,
                image = "",
                isFavorite = true,
                creator = UserProfile(
                    name = "Lorem",
                    lastName = "Ipsum",
                    username = "lorem_ipsum",
                    photo = ""
                ),
                createdAt = Date()
            ),
            Receipt(
                id = "test",
                title = "Test receipt",
                timeToPrepare = 60,
                image = "",
                isFavorite = true,
                creator = UserProfile(
                    name = "Lorem",
                    lastName = "Ipsum",
                    username = "lorem_ipsum",
                    photo = ""
                ),
                createdAt = Date()
            ),
        ),
            )
        return try {
            Result.Success(remoteDataSource.loadReceipts(page <= 1))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteReceipt(receiptId: String) {
        TODO("Not yet implemented")
    }
}