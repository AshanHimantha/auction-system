<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script src="https://cdn.tailwindcss.com"></script>
<script>
    tailwind.config = {
        darkMode: 'class',
        theme: {
            extend: {
                colors: {
                    border: "hsl(240 5.9% 15%)",
                    input: "hsl(240 3.7% 15.9%)",
                    ring: "hsl(240 4.9% 83.9%)",
                    background: "hsl(240 10% 3.9%)",
                    foreground: "hsl(0 0% 98%)",
                    primary: {
                        DEFAULT: "hsl(240 5.9% 90%)",
                        foreground: "hsl(240 5.9% 10%)",
                    },
                    secondary: {
                        DEFAULT: "hsl(240 3.7% 15.9%)",
                        foreground: "hsl(0 0% 98%)",
                    },
                    accent: {
                        DEFAULT: "hsl(240 3.7% 15.9%)",
                        foreground: "hsl(0 0% 98%)",
                    },
                    card: {
                        DEFAULT: "hsl(240 10% 5%)",
                        foreground: "hsl(0 0% 98%)",
                    },
                    muted: {
                        DEFAULT: "hsl(240 3.7% 15.9%)",
                        foreground: "hsl(240 5% 64.9%)",
                    },
                }
            }
        }
    }
</script>
<link rel="stylesheet" href="../css/styles.css">