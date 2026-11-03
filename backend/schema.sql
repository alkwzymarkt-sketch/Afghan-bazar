-- =====================================================================
-- ALKOZI MARKETPLACE & ALKOZI ADMIN CENTRAL DATABASE SCHEMA (PostgreSQL)
-- Complete Production DDL for 34 Provinces of Afghanistan Marketplace
-- =====================================================================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. ENUMS
CREATE TYPE user_role AS ENUM (
    'customer',
    'seller',
    'support_manager',
    'finance_manager',
    'product_moderator',
    'general_admin',
    'super_admin'
);

CREATE TYPE user_status AS ENUM ('active', 'suspended', 'pending_verification');
CREATE TYPE order_status AS ENUM ('pending', 'confirmed', 'processing', 'shipped', 'delivered', 'cancelled');
CREATE TYPE payment_status AS ENUM ('pending', 'paid', 'failed', 'refunded');
CREATE TYPE telecom_operator AS ENUM ('awcc', 'roshan', 'etisalat', 'mtn', 'salaam');
CREATE TYPE subscription_plan AS ENUM ('free', 'monthly', 'quarterly', 'yearly');

-- 2. USERS TABLE
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    full_name VARCHAR(150) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    operator telecom_operator NOT NULL,
    role user_role DEFAULT 'customer',
    status user_status DEFAULT 'active',
    province VARCHAR(50) NOT NULL,
    city VARCHAR(100),
    avatar_url TEXT,
    wallet_balance_afn BIGINT DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_phone ON users(phone);
CREATE INDEX idx_users_province ON users(province);
CREATE INDEX idx_users_role ON users(role);

-- 3. PLATFORM GLOBAL SETTINGS (Controlled by Alkozi Admin)
CREATE TABLE platform_settings (
    id INTEGER PRIMARY KEY DEFAULT 1,
    platform_name VARCHAR(100) DEFAULT 'Alkozi Market',
    support_phone VARCHAR(20) DEFAULT '+93 700 000 000',
    currency_code VARCHAR(10) DEFAULT 'AFN',
    currency_symbol VARCHAR(10) DEFAULT 'افغانی',
    
    -- Subscription Switch & Pricing (Crucial Requirement: Full Admin Control)
    is_free_subscription_enabled BOOLEAN DEFAULT TRUE,
    monthly_subscription_fee_afn BIGINT DEFAULT 1500,
    quarterly_subscription_fee_afn BIGINT DEFAULT 4000,
    yearly_subscription_fee_afn BIGINT DEFAULT 14000,
    
    -- Payment Gateways Switch
    cod_enabled BOOLEAN DEFAULT TRUE,
    mpaisa_enabled BOOLEAN DEFAULT TRUE,
    mymoney_enabled BOOLEAN DEFAULT TRUE,
    bank_transfer_enabled BOOLEAN DEFAULT TRUE,
    
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT single_row_check CHECK (id = 1)
);

-- Seed default platform settings
INSERT INTO platform_settings (id, is_free_subscription_enabled, monthly_subscription_fee_afn)
VALUES (1, TRUE, 1500)
ON CONFLICT (id) DO NOTHING;

-- 4. SHOPS / VENDOR BOOTHS (Marketplace)
CREATE TABLE shops (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    owner_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(150) NOT NULL,
    business_phone VARCHAR(20) NOT NULL,
    logo_url TEXT,
    banner_url TEXT,
    description TEXT,
    address TEXT NOT NULL,
    province VARCHAR(50) NOT NULL,
    city VARCHAR(100) NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    
    -- Seller Subscription Management
    is_exempt_from_subscription BOOLEAN DEFAULT FALSE,
    current_plan subscription_plan DEFAULT 'free',
    subscription_expires_at TIMESTAMP WITH TIME ZONE,
    
    total_sales_count INT DEFAULT 0,
    total_revenue_afn BIGINT DEFAULT 0,
    rating NUMERIC(3, 2) DEFAULT 5.00,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_shops_owner ON shops(owner_id);
CREATE INDEX idx_shops_province ON shops(province);

-- 5. CATEGORIES TABLE (16 Default Categories)
CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    key_name VARCHAR(50) NOT NULL UNIQUE,
    title_fa VARCHAR(100) NOT NULL,
    title_ps VARCHAR(100) NOT NULL,
    title_en VARCHAR(100) NOT NULL,
    icon_name VARCHAR(50),
    sort_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE
);

-- Seed 16 official default categories
INSERT INTO categories (key_name, title_fa, title_ps, title_en, sort_order) VALUES
('mobile', 'موبایل و تبلت', 'موبایل او ټابلیټ', 'Mobile & Tablets', 1),
('computers', 'کامپیوتر و لپ‌تاپ', 'کمپیوټر او لپټاپ', 'Computers & Laptops', 2),
('home_appliances', 'لوازم خانه', 'د کور لوازم', 'Home Appliances', 3),
('clothing', 'لباس و پوشاک', 'کالي او پوښاک', 'Clothing & Apparel', 4),
('shoes_bags', 'کفش و کیف', 'بوټان او بکسونه', 'Shoes & Bags', 5),
('vehicles', 'وسایل نقلیه', 'نقلیه وسایط', 'Vehicles & Cars', 6),
('car_parts', 'لوازم و پرزه‌جات موتر', 'د موټر پرزې او لوازم', 'Car Parts & Accessories', 7),
('groceries', 'مواد غذایی', 'خوراکي توکي', 'Groceries & Food', 8),
('cosmetics', 'لوازم آرایشی', 'سینګار توکي', 'Cosmetics & Beauty', 9),
('baby_kids', 'لوازم کودک', 'د ماشومانو توکي', 'Baby & Kids', 10),
('sports', 'وسایل ورزشی', 'ورزشي وسایل', 'Sports & Fitness', 11),
('construction', 'تجهیزات ساختمانی', 'ساختماني وسایل', 'Construction Materials', 12),
('services', 'خدمات', 'خدمتونه', 'Services', 13),
('stationery', 'کتاب و لوازم تحریر', 'کتابونه او قرطاسیه', 'Books & Stationery', 14),
('agriculture', 'محصولات زراعتی', 'کرنیز محصولات', 'Agricultural Products', 15),
('others', 'سایر محصولات', 'نور محصولات', 'Other Products', 16)
ON CONFLICT (key_name) DO NOTHING;

-- 6. PRODUCTS TABLE
CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    shop_id UUID NOT NULL REFERENCES shops(id) ON DELETE CASCADE,
    category_id INT NOT NULL REFERENCES categories(id),
    title VARCHAR(250) NOT NULL,
    description TEXT,
    price_afn BIGINT NOT NULL CHECK (price_afn >= 0),
    original_price_afn BIGINT DEFAULT 0,
    discount_percent INT DEFAULT 0,
    stock INT NOT NULL DEFAULT 1 CHECK (stock >= 0),
    province VARCHAR(50) NOT NULL,
    city VARCHAR(100),
    is_sponsored BOOLEAN DEFAULT FALSE,
    is_special_offer BOOLEAN DEFAULT FALSE,
    is_approved BOOLEAN DEFAULT TRUE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_products_shop ON products(shop_id);
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_province ON products(province);
CREATE INDEX idx_products_sponsored ON products(is_sponsored);

-- 7. PRODUCT IMAGES
CREATE TABLE product_images (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    image_url TEXT NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    sort_order INT DEFAULT 0
);

-- 8. ORDERS & ORDER ITEMS
CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_number VARCHAR(30) NOT NULL UNIQUE,
    buyer_id UUID NOT NULL REFERENCES users(id),
    shop_id UUID NOT NULL REFERENCES shops(id),
    subtotal_afn BIGINT NOT NULL,
    discount_afn BIGINT DEFAULT 0,
    shipping_fee_afn BIGINT DEFAULT 0,
    total_amount_afn BIGINT NOT NULL,
    province VARCHAR(50) NOT NULL,
    delivery_address TEXT NOT NULL,
    customer_phone VARCHAR(20) NOT NULL,
    payment_method VARCHAR(50) DEFAULT 'COD',
    payment_status payment_status DEFAULT 'pending',
    order_status order_status DEFAULT 'pending',
    tracking_code VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE order_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id UUID NOT NULL REFERENCES products(id),
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price_afn BIGINT NOT NULL
);

-- 9. SELLER SUBSCRIPTION PAYMENTS (When free subscription is disabled by Admin)
CREATE TABLE seller_subscriptions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    shop_id UUID NOT NULL REFERENCES shops(id) ON DELETE CASCADE,
    plan_type subscription_plan NOT NULL,
    fee_afn BIGINT NOT NULL,
    starts_at TIMESTAMP WITH TIME ZONE NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    payment_method VARCHAR(50),
    status payment_status DEFAULT 'paid',
    granted_by_admin_id UUID REFERENCES users(id),
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 10. REAL-TIME CHAT (WebSocket between Buyer & Seller)
CREATE TABLE chat_rooms (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    buyer_id UUID NOT NULL REFERENCES users(id),
    seller_id UUID NOT NULL REFERENCES users(id),
    shop_id UUID NOT NULL REFERENCES shops(id),
    product_id UUID REFERENCES products(id),
    last_message TEXT,
    last_message_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (buyer_id, seller_id, shop_id)
);

CREATE TABLE chat_messages (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    room_id UUID NOT NULL REFERENCES chat_rooms(id) ON DELETE CASCADE,
    sender_id UUID NOT NULL REFERENCES users(id),
    message_text TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 11. AUDIT LOGS (Admin Activity & Security)
CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    admin_id UUID NOT NULL REFERENCES users(id),
    action_type VARCHAR(100) NOT NULL,
    entity_name VARCHAR(100) NOT NULL,
    entity_id VARCHAR(100),
    details JSONB,
    ip_address VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 12. OTP VERIFICATIONS (With Redis fallback)
CREATE TABLE otp_verifications (
    phone VARCHAR(20) PRIMARY KEY,
    code_hash VARCHAR(100) NOT NULL,
    operator telecom_operator NOT NULL,
    attempts INT DEFAULT 1,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 13. IN-APP ADVERTISING MANAGEMENT SYSTEM
CREATE TYPE ad_target_type AS ENUM ('shop', 'product', 'company', 'service');
CREATE TYPE ad_placement_area AS ENUM (
    'HOME_PAGE',        -- Main home slider/banner
    'TOP_PRODUCTS',     -- Top of the product listing
    'CATEGORIES',       -- Inside category browsing
    'SHOPS_PAGE',       -- Shops listing screen
    'FEATURED_SECTION'  -- Dedicated special promo section
);

CREATE TABLE advertisements (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(200) NOT NULL,
    description TEXT,
    banner_image_url TEXT NOT NULL,
    
    -- Target & Relations
    target_type ad_target_type NOT NULL DEFAULT 'company',
    placement ad_placement_area NOT NULL DEFAULT 'HOME_PAGE',
    shop_id UUID REFERENCES shops(id) ON DELETE SET NULL,
    product_id UUID REFERENCES products(id) ON DELETE SET NULL,
    
    -- Advertiser Contact & Action Links
    advertiser_phone VARCHAR(30),
    whatsapp_link TEXT,
    website_url TEXT,
    
    -- Commercial Information
    fee_charged_afn BIGINT NOT NULL DEFAULT 0 CHECK (fee_charged_afn >= 0),
    start_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE NOT NULL,
    
    -- Real-time Controls & Analytics
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    impressions_count BIGINT DEFAULT 0,
    clicks_count BIGINT DEFAULT 0,
    
    -- Administrative Relations
    created_by_admin_id UUID REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_ad_dates CHECK (end_date >= start_date)
);

-- Fast Indexing for Public Mobile Apps & Filter Queries
CREATE INDEX idx_ads_placement_active ON advertisements(placement, is_active, start_date, end_date);
CREATE INDEX idx_ads_active_range ON advertisements(is_active, start_date, end_date);
CREATE INDEX idx_ads_shop_id ON advertisements(shop_id);
CREATE INDEX idx_ads_product_id ON advertisements(product_id);

