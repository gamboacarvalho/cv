﻿using Isel.Ave14.SqlMapper;
using System;
using System.Collections.Generic;

namespace SqlMapperTest
{
    public class Product : IEquatable<Product>
    {
        [SqlPk] public int ProductID { set; get; }
        public string ProductName { set; get; }
        public int SupplierID { set; get; }
        public string QuantityPerUnit { set; get; }
        public decimal UnitPrice { set; get; }
        public short UnitsInStock { set; get; }
        public short UnitsOnOrder { set; get; }
        public short ReorderLevel { set; get; }
        public bool Discontinued { set; get; }
/*
        public Product(
            int productId,
            string productName,
            decimal unitPrice,
            short unitsInStock)
        {
            ProductID = productId;
            ProductName = productName;
            UnitPrice = unitPrice;
            UnitsInStock = unitsInStock;
        }
*/

        public Product()
        { 
        }

        public Product(
            IEnumerable<Category> categories, 
            bool discontinued, 
            int productId, 
            string productName,
            string quantityPerUnit, 
            short reorderLevel, 
            int supplierId, 
            decimal unitPrice,
            short unitsInStock, 
            short unitsOnOrder)
        {
            Discontinued = discontinued;
            ProductID = productId;
            ProductName = productName;
            QuantityPerUnit = quantityPerUnit;
            ReorderLevel = reorderLevel;
            SupplierID = supplierId;
            UnitPrice = unitPrice;
            UnitsInStock = unitsInStock;
            UnitsOnOrder = unitsOnOrder;
        }

        public bool Equals(Product other)
        {
            if (ReferenceEquals(null, other)) return false;
            if (ReferenceEquals(this, other)) return true;
            return ProductID == other.ProductID && string.Equals(ProductName, other.ProductName) && SupplierID == other.SupplierID && string.Equals(QuantityPerUnit, other.QuantityPerUnit) && UnitPrice == other.UnitPrice && UnitsInStock == other.UnitsInStock && UnitsOnOrder == other.UnitsOnOrder && ReorderLevel == other.ReorderLevel && Discontinued.Equals(other.Discontinued);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != this.GetType()) return false;
            return Equals((Product)obj);
        }

        public override int GetHashCode()
        {
            unchecked
            {
                int hashCode = ProductID;
                hashCode = (hashCode * 397) ^ (ProductName != null ? ProductName.GetHashCode() : 0);
                hashCode = (hashCode * 397) ^ SupplierID;
                hashCode = (hashCode * 397) ^ (QuantityPerUnit != null ? QuantityPerUnit.GetHashCode() : 0);
                hashCode = (hashCode * 397) ^ UnitPrice.GetHashCode();
                hashCode = (hashCode * 397) ^ UnitsInStock.GetHashCode();
                hashCode = (hashCode * 397) ^ UnitsOnOrder.GetHashCode();
                hashCode = (hashCode * 397) ^ ReorderLevel.GetHashCode();
                hashCode = (hashCode * 397) ^ Discontinued.GetHashCode();
                return hashCode;
            }
        }


    public override string ToString()
        {
            return "Product [id=" + ProductID + ", name=" + ProductName + ", price=" + UnitPrice
                    + ", stock=" + UnitsInStock + "]";
        }
    }
}
