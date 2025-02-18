<template>
    <button
        tabindex="0"
        :class="`btn ${convertButtonStyleToClass(props.style)}`"
        style="min-width: 150px"
        @click="action"
    >
        {{ text }}
    </button>
</template>

<script setup lang="ts">
export type ButtonStyle = "primary" | "secondary"

defineOptions({inheritAttrs: false})
const props = defineProps<{
    text: string,
    action: () => void
    style: ButtonStyle
}>()

function convertButtonStyleToClass(style: ButtonStyle) {
    switch (style) {
        case "primary": return "btn-primary"
        case "secondary": return "btn-secondary"
    }
}
</script>