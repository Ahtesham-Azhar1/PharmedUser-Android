package com.zealsoft.phamred_user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zealsoft.pharmed.Util.Constants
import com.zealsoft.pharmed.activities.CartKActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        openCart()
    }

    private fun openCart() {

        var intent = Intent(this, CartKActivity::class.java)
        intent.putExtra(Constants.INTENT_DEVICE_ID, "dda59672-d5cd-446a-93fe-1c62cb970d42123")
        intent.putExtra(Constants.INTENT_USER_ID, "5f0d9527ab7426d5072a07a5")
        intent.putExtra(Constants.INTENT_TOKEN, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwaGFybWFjeUlkIjoiNWVkZjZmZDhkNmY3ZGM0NDk3YWNmZDE5Iiwicm9sZSI6InBoYXJtYWN5IiwiZmNtVG9rZW4iOlsiY3J6SUR1MG9SNk8wTFB0RmthdU9IYTpBUEE5MWJINVlKLWNQQUZzNzlrYVRuQUZmQmFkdU5wNjdwWml3czVQXzJVWm5GWWVEaDhwSVVrZkVnSXB3VWFpS0MySmFoTElZYXpNVGI0cWRFRDdFeUpDeHJaTDN1Ri1WQU9CeHZmNFhZR3hvcTkyaUc1QkhCcU5PRW5jSkhDZV9NS1FQYXY2SVp2diIsImZNMUZycl93UmlxQ1Jha1RaWEpuR0k6QVBBOTFiRzQybWNjSHViSnlxXzR2RFZZdjhXMG1GYzExWTBSZFIwZ1JsdlBkTTh6RFdTVy04S3ktSkRpU0ZkMW9hZXZocTZZQVFHTW9QazdUQmhWT2Y2QVBvY1JCNzhIcldPMnlrT1pGeUV0bmZTbXFXV1ctS1R0SkxMV05aWTdGTEs3SEZyeU15VlgiXSwidG9rZW5FeHBpcnlEYXRlIjoiMjAyMC0xMi0xMFQwMzoyNDoyMy41ODZaIiwic2Vzc2lvbklkIjoiNWVlMGZiMjdmZjgxNzUwYjI2ZWQxNDk1IiwiaWF0IjoxNTkxODAyNjYzfQ.jDXQPVyrd-IwuFAlGFgvLpmgX_vlyaAJ1-8JDyihet4")
        startActivity(intent)
    }
}
