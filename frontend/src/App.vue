<template>
    <header role="banner" aria-labelledby="heading" ref="header" class="navbar fixed-top bg-white shadow-sm" style="min-height: 70px">
        <button tabindex="0" v-if="buttonData.length > 0" @click="focusFooter" class="btn btn-link bg-white shadow skip-to-footer"> Zur Fußzeile springen</button>
        <div class="container d-flex justify-content-center align-items-center">
            <div class="navbar-brand">
                <template v-if="titles.length === 0">
                    <h1 class="fs-3 fw-bold m-0 text-center">LearnEasy</h1>
                </template>
                <template v-else>
                    <h1 id="heading" class="fs-3 fw-regular mb-0 text-center" v-html="titles.join('<br>')"/>
                </template>
            </div>
        </div>
    </header>

    <main role="main" :aria-live="liveRegion" ref="main" class="main-content container d-flex flex-column justify-content-center align-items-center py-0 gap-4"
          :style="{ marginTop: headerHeight + 40 + 'px' }" style="width: 90%; max-width: 600px; margin-bottom: 100px">
        <RouterView @route="onRouteChange"/>
    </main>

    <footer role="navigation" ref="footer" class="navbar mt-auto fixed-bottom bg-white" style="height: 70px; box-shadow: 0 -0.125rem 0.25rem rgba(0, 0, 0, 0.075)">
        <div class="container d-flex justify-content-center gap-3">
            <template v-if="buttonData.length !== 0" v-for="buttonData in buttonData">
                <GeneralButton :text="buttonData.text" :action="buttonData.action" :style="buttonData.type"/>
            </template>
        </div>
        <button tabindex="0" @click="focusMain" class="btn btn-link bg-white shadow skip-to-main-content"> Zum Hauptinhalt springen</button>
    </footer>
</template>

<script setup lang="ts">
import {RouterView, useRouter} from "vue-router"
import {nextTick, onBeforeUnmount, onMounted, ref, type Ref} from "vue";
import GeneralButton, {type ButtonStyle} from "@/components/GeneralButton.vue";

export type RouteEmit = { (event: "route", routeData: RouteData): void }
export type RouteData = { titles: Array<string>, buttonData: Array<ButtonData>, liveRegion?: "assertive" | "off" | "polite" }
export type ButtonData = { text: string, action: () => void, type: ButtonStyle }

const titles: Ref<Array<string>> = ref([])
const buttonData: Ref<Array<ButtonData>> = ref([])
const liveRegion: Ref<"assertive" | "off" | "polite"> = ref("off")

function onRouteChange(routeData: RouteData) {
    titles.value = routeData.titles ?? ["LearnEasy"];
    buttonData.value = routeData.buttonData ?? [];
    liveRegion.value= routeData.liveRegion ?? "off";

    document.body.setAttribute("tabindex", "-1");
    document.body.focus();
    document.body.removeAttribute("tabindex");
}

const header = ref<HTMLElement | null>(null)
const headerHeight = ref<number>(0)
const main = ref<HTMLElement | null>(null)
const footer = ref<HTMLElement | null>(null)
const scrollIntoTheMiddle = (event: Event) => {
    if(event.target instanceof Element){
        event.target.scrollIntoView({ behavior: 'smooth', block: "center"})
    }
}
let resizeObserver: ResizeObserver

function focusMain() {
    if (main.value) main.value.querySelector<HTMLElement>('[tabindex="0"]')?.focus()
}

function focusFooter() {
    if (footer.value) footer.value.querySelector<HTMLElement>('[tabindex="0"]')?.focus()
}

onMounted(() => {
    if (header.value) {
        resizeObserver = new ResizeObserver(entries => {
            for (const entry of entries) {
                if (entry.contentRect) headerHeight.value = entry.contentRect.height
            }
        })
        resizeObserver.observe(header.value)
    }

    main.value?.addEventListener('focusin', scrollIntoTheMiddle);
})

onBeforeUnmount(() => {
    if (resizeObserver && header.value) resizeObserver.unobserve(header.value)
    main.value?.removeEventListener('focusin', scrollIntoTheMiddle);
})
</script>

<style scoped>
.skip-to-footer {
    position: absolute;
    left: -9999px;
    z-index: 999;
    padding: 1em;
    opacity: 0;
}
.skip-to-footer:focus {
    margin: 5px;
    left: 0;
    top: 0;
    z-index: 2000;
    opacity: 1;
}

.skip-to-main-content {
    position: absolute;
    z-index: 999;
    right: -100%;
    bottom: 0;
    padding: 1em;
    opacity: 0;
}

.skip-to-main-content:focus {
    margin: 5px;
    right: 0;
    bottom: 0;
    z-index: 2000;
    opacity: 1;
}
</style>
