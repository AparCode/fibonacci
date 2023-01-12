import { Product } from "./product";

export interface ShoppingCart {
    user: string,
    items: Product[]
    reservation: Product
}